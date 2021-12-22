package net.atcat.nanzhi.im3;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NZPigeonSit {

    protected static EntityType<SitEntity> SIT_ENTITY = null ;
    public static final Event event = new Event( ) ;
    public static IAPI api = new DenyAPI( ) ;

    // 事件
    public static class Event {
        public enum EventType {
            too_crowded , // 坐标中的玩家过于拥挤导致的无法坐下
            can_not_sit , // 可能是没通过坐标合理性检查导致的不能坐下
            wrong_pose , // 错误的姿势导致玩家没法坐下
        } ;
        protected Map<EventType,List<Consumer<PlayerEntity>>> on = new HashMap<>( ) ;
        public Event ( ) {
            for ( EventType event : EventType.values() ) {
                this.on.put( event, new ArrayList<>( ) ) ;
            } ;
        } ;
        public Event on( EventType event, Consumer<PlayerEntity> callback ) {
            List<Consumer<PlayerEntity>> list = this.on.get( event ) ;
            if ( list != null )
                list.add( callback ) ;
            return this ;
        } ;
        public Event call(EventType event, PlayerEntity player ) {
            List<Consumer<PlayerEntity>> list = this.on.get( event ) ;
            if ( list != null )
                for ( Consumer<PlayerEntity> callback : list )
                    callback.accept( player ) ;
            return this ;
        } ;
    } ;
    // 开放接口
    public interface IAPI {
        /** 检查区域内是否有生物坐着
         *
         * @param world    世界
         * @param pos      检查坐标
         * @return         结果
         */
        public default boolean isLivingEntitySittingOnPos( IWorld world, BlockPos pos ) {
            return this.isLivingEntitySittingOnPos( world, pos.getX( ), pos.getY( ), pos.getZ( ) ) ;
        }
        // 同上，只不过坐标换成了3个值。
        public boolean isLivingEntitySittingOnPos( IWorld world, double posX, double posY, double posZ ) ;
        /** 让生物坐在指定坐标
         *  预留ignoreCrowded是因为椅子方块不会检查，因为椅子方块自己会检查，并且不会提示
         *
         * @param entity   生物
         * @param pos      坐标
         * @param sendMessage   无法坐下时是否发送信息
         * @return   椅子实体
         */
        @Nullable
        public default SitEntity sitToChair ( LivingEntity entity, Vector3d pos, boolean sendMessage ) {
            return this.sitToChair( entity, pos.x( ), pos.y( ), pos.z( ), sendMessage ) ;
        }
        @Nullable // 同上，只不过坐标换成了3个值。
        public SitEntity sitToChair ( LivingEntity entity, double x, double y, double z, boolean sendMessage ) ;
        @Nullable // 让生物坐在原地
        public default SitEntity sitToChair (LivingEntity entity ) {
            return this.sitToChair( entity, entity.position( ), true ) ;
        } ;
        /** 坐下的合理性判断
         *
         * @param world    世界
         * @param pos      检测的位置，通常是乘客或者自身的位置
         * @param passengersPose   乘客的姿势
         * @return   是否通过校验
         */
        public default boolean isValidityLocation( IWorld world, Vector3d pos, Pose passengersPose ) {
            return this.isValidityLocation( world, new BlockPos( Math.floor( pos.x( ) ), Math.floor( pos.y( ) - 0.03 ), Math.floor( pos.z( ) ) ), passengersPose ) ;
        } ;
        // 同上，只不过坐标换成了3个值。
        public default boolean isValidityLocation( IWorld world, BlockPos pos, Pose passengersPose ) {
            return !world.getBlockState( pos ).getMaterial( ).equals( Material.AIR ) && ( passengersPose.equals( Pose.STANDING ) || passengersPose.equals( Pose.CROUCHING ) ) ;
        } ;
        // 同上，只不过检测是以生物的位置作为判断。
        public default boolean isValidityLocation( LivingEntity entity ) {
            return entity.isOnGround( ) && this.isValidityLocation( entity.level, entity.position( ), entity.getPose( ) ) ;
        } ;
    } ;
    // 注册
    public static EntityType<SitEntity> registry ( DeferredRegister<EntityType<?>> defReg ) {
        if ( SIT_ENTITY == null ) {
            SIT_ENTITY = register( defReg, SitEntity.ID, ( a, b ) -> { return new SitEntity( a, b ) ; }, EntityClassification.MISC, SitEntity.WIDTH, SitEntity.HEIGHT ) ;
            NZPigeonSit.api = new API( ) ;
        } ;
        return SIT_ENTITY ;
    } ;
    // 椅子方块
    public static class ChairBlock extends HorizontalBlock {
        // 默认属性
        public static final Properties DEF_PROPERTIES = Properties.of( Material.WOOD ).strength( 2 ).sound( SoundType.WOOD ) ;
        public static final Direction DEF_DIRECTION = Direction.NORTH ;
        // 固定属性
        protected final Direction defaultDirection ;
        protected final double[] sitPos = new double[]{ 0.5, 0.5, 0.5 }; // 原始坐标
        // 实例方法
        public ChairBlock( Properties properties ) {
            super( properties ) ;
            this.defaultDirection = this.getDefaultDirection( ) ;
            this.registerDefaultState( this.stateDefinition.any( ).setValue( FACING, this.defaultDirection ) ) ;
        }
        public ChairBlock( ) {
            this( DEF_PROPERTIES ) ;
        }
        // 设置坐下的位置的方法，坐标的区间为 [0,1]。
        public ChairBlock setSitPos ( double x, double y, double z ) {
            this.sitPos[0] = x ;
            this.sitPos[1] = y ;
            this.sitPos[2] = z ;
            return this ;
        } ;
        // 当前的方块正面是哪个方向？
        public Direction getDefaultDirection( ) {
            return DEF_DIRECTION ;
        } ;
        @Override // 碰撞箱
        public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context ) {
            return super.getShape( state, reader, pos, context ) ;
        }
        @Override // 使用
        public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result ) {
            if ( !world.isClientSide( ) ) {
                Direction facing = state.getValue( FACING ) ;
                RotatePos.RotateAngle ra = RotatePos.RotateAngle.get( ( facing.get2DDataValue( ) -DEF_DIRECTION.get2DDataValue( ) ) & 3 ) ;
                // 这里写反是因为奇奇怪怪的mojang的奇奇怪怪的模型坐标
                double[] rotate = RotatePos.rotate( this.sitPos[2], this.sitPos[0], 0.5d, ra ) ;
                // 没有生物坐下时
                if ( !api.isLivingEntitySittingOnPos( world, pos ) ) {
                    api.sitToChair( player, pos.getX( ) +rotate[0], pos.getY( ) +this.sitPos[1], pos.getZ( ) +rotate[1], false ) ;
                } else {
                    return ActionResultType.FAIL ;
                } ;
            }
            return ActionResultType.SUCCESS ;
        }
        @Override // 属性
        protected void createBlockStateDefinition( StateContainer.Builder<Block, BlockState> builder ) {
            builder.add( FACING ) ;
            super.createBlockStateDefinition( builder ) ;
        }
        @Override // 放置
        public BlockState getStateForPlacement( BlockItemUseContext context ) {
            return this.defaultBlockState( ).setValue( FACING, context.getHorizontalDirection( ).getOpposite( ) ) ;
        }

    }

    // - - - - - - - - - - - - - - > 以下的内容不是你应该关心的内容，除非你有其他需求 < - - - - - - - - - - - - - - //

    // 生物
    public static class SitEntity extends Entity {
        // 默认生物体积，有需求在这里自己改
        public static final float HEIGHT = 0.02f ;
        public static final float WIDTH = 0.4f ;
        public static final String ID = "nzpigeon_api_sit" ;
        //不需要保存
        private int checkTick = 0 ;
        // 快速建立，坐标自己设置
        public SitEntity( EntityType<?> entityTypeIn, World world ) {
            super( entityTypeIn, world ) ;
            this.setInvulnerable( true ) ;
        }
        // 传入世界，x，y，z
        public SitEntity( World world, double x, double y, double z ) {
            this( SIT_ENTITY, world ) ;
            this.setPos( x, y, z );
            this.xo = x ;
            this.yo = y ;
            this.zo = z ;
        }
        @Override
        public void tick( ) {
            if ( !this.level.isClientSide ) { // 不处理客户端
                if ( this.getPassengers( ).size( ) == 0 ) { this.remove( ) ; } // 没有乘客则销毁自身
                else if ( this.checkTick == 0 ) { // 执行刻且玩家姿势不对则销毁自身
                    if ( !api.isValidityLocation( this.level, this.position( ), this.getPassengers( ).get( 0 ).getPose( ) ) )
                        this.remove( ) ;
                } ;
                this.checkTick = ( this.checkTick +1 ) &15 ;
            }
            super.tick( ) ;
        }
        @Override // 偏移量
        public double getPassengersRidingOffset( ) { return (double)this.getEyeHeight( ) -0.25f; }
        @Override // 眼睛高度
        protected float getEyeHeight(Pose pose, EntitySize size ) { return size.height ; }
        @Override // 不管，接口需要
        protected void defineSynchedData( ) { }
        @Override
        protected void readAdditionalSaveData( CompoundNBT compoundNBT ) { }
        @Override
        protected void addAdditionalSaveData( CompoundNBT compoundNBT ) { }
        @Override
        public IPacket<?> getAddEntityPacket( ) {
            return NetworkHooks.getEntitySpawningPacket( this ) ;
        }
    } ;
    // 这个接口什么都不会做
    public static class DenyAPI implements IAPI {
        @Override
        public boolean isLivingEntitySittingOnPos( IWorld world, double posX, double posY, double posZ ) {
            return false ;
        }
        @Nullable
        @Override
        public SitEntity sitToChair( LivingEntity entity, double x, double y, double z, boolean sendMessage ) {
            return null ;
        }
    }
    // 这个接口是个可用的接口
    public static class API implements IAPI {
        @Override
        public boolean isLivingEntitySittingOnPos( IWorld world, double posX, double posY, double posZ ) {
            List<SitEntity> list = world.getEntitiesOfClass( SitEntity.class, new AxisAlignedBB( posX +0.0625, posY, posZ +0.0625, posX +0.9375, posY +1.5, posZ +0.9375 ) ) ;
            return list.size( ) != 0 ;
        }
        @Nullable
        @Override
        public SitEntity sitToChair( LivingEntity entity, double x, double y, double z, boolean sendMessage ) {
            // 不处理尸体
            if ( !entity.isAlive( ) ) { return null ; } ;
            if ( isLivingEntitySittingOnPos( entity.level, x, y, z ) ) { // 有生物坐着
                // 通知
                if ( sendMessage && entity instanceof PlayerEntity )
                    event.call( Event.EventType.too_crowded, (PlayerEntity) entity ) ;
            } ;
            // 生成座椅
            SitEntity chair = new SitEntity( entity.level, x, y, z ) ;
            // 添加座椅
            entity.level.addFreshEntity( chair ) ;
            // 解除乘坐状态
            if ( entity.isPassenger( ) ) { entity.stopRiding( ) ; } ;
            // 让生物坐上去
            entity.startRiding( chair ) ;
            return chair ;
        }
    }
    // 坐标旋转控制
    public static class RotatePos {
        public enum RotateAngle {
            R0(0), R90(1), R180(2), R270(3);
            public final int id  ;
            RotateAngle( int id ) {
                this.id = id ;
            }
            public static RotateAngle get (int id ) {
                switch ( id ) {
                    case 1:
                        return R90 ;
                    case 2:
                        return R180 ;
                    case 3:
                        return R270 ;
                    default:
                        return R0 ;
                }
            } ;
        } ;
        public static double[] rotate ( double x, double z, double center, RotateAngle rotate ) {
            double[] ret = new double[2] ;
            double lx = x -center ;
            double lz = z -center ;
            switch ( rotate ) {
                case R90 :
                    ret[0] = -lz ;
                    ret[1] = lx ;
                    break ;
                case R180 :
                    ret[0] = -lx ;
                    ret[1] = -lz ;
                    break ;
                case R270 :
                    ret[0] = lz ;
                    ret[1] = -lx ;
                    break ;
                case R0 :
                    ret[0] = lx ;
                    ret[1] = lz ;
                    break ;
            } ;
            ret[0] += center ;
            ret[1] += center ;
            return ret ;
        } ;
        public static float[] rotate ( float x, float z, float center, RotateAngle rotate ) {
            float[] ret = new float[2] ;
            float lx = x -center ;
            float lz = z -center ;
            switch ( rotate ) {
                case R90 :
                    ret[0] = -lz ;
                    ret[1] = lx ;
                    break ;
                case R180 :
                    ret[0] = -lx ;
                    ret[1] = -lz ;
                    break ;
                case R270 :
                    ret[0] = lz ;
                    ret[1] = -lx ;
                    break ;
                case R0 :
                    ret[0] = lx ;
                    ret[1] = lz ;
                    break ;
            } ;
            ret[0] += center ;
            ret[1] += center ;
            return ret ;
        } ;
        public static int[] rotate ( int x, int z, int center, RotateAngle rotate ) {
            int[] ret = new int[2] ;
            int lx = x -center ;
            int lz = z -center ;
            switch ( rotate ) {
                case R90 :
                    ret[0] = -lz ;
                    ret[1] = lx ;
                    break ;
                case R180 :
                    ret[0] = -lx ;
                    ret[1] = -lz ;
                    break ;
                case R270 :
                    ret[0] = lz ;
                    ret[1] = -lx ;
                    break ;
                case R0 :
                    ret[0] = lx ;
                    ret[1] = lz ;
                    break ;
            } ;
            ret[0] += center ;
            ret[1] += center ;
            return ret ;
        } ;

    }
    // 包装
    private static <T extends Entity> EntityType<T> register ( DeferredRegister<EntityType<?>> defReg, String name, EntityType.IFactory<T> ts, EntityClassification classification, float width, float height ) {
        EntityType<T> entity = EntityType.Builder.of( ts, classification ).sized( width, height ).build( name ) ;
        defReg.register( name, ( ) -> entity ) ;
        return entity ;
    }

}
