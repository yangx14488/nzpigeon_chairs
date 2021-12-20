package net.atcat.nanzhi.chairs.com.block;

import net.atcat.nanzhi.chairs.com.RotateVoxelShape;
import net.atcat.nanzhi.chairs.com.func.LivingEntitySit;
import net.atcat.nanzhi.chairs.com.func.RotatePos;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class Chair extends HorizontalBlock {

    public static final Properties DEF_PROPERTIES = Properties.of( Material.STONE ).strength( 2 ) ;
    public static final Direction DEF_DIRECTION = Direction.NORTH ;
    public static final RotateVoxelShape DEF_ROTATE_SHAPE = new RotateVoxelShape( ) ;

    public static class Config {
        protected double[] sitPos = new double[3] ; // 原始坐标
        protected RotateVoxelShape rotateShape = DEF_ROTATE_SHAPE ;
        protected Properties properties = DEF_PROPERTIES ;
        protected Direction defaultDirection = DEF_DIRECTION ;
        // 设置坐标
        public Config sitPos ( double x, double y, double z ) {
            this.sitPos[0] = x ;
            this.sitPos[1] = y ;
            this.sitPos[2] = z ;
            return this ;
        } ;
        public Config sitPos ( double[] pos ) {
            if ( pos != null && pos.length == 3 ) {
                this.sitPos = pos.clone( ) ;
            } ;
            return this ;
        } ;
        public Config chairHeight ( double y ) {
            this.sitPos[1] = y ;
            return this ;
        }
        // 设置旋转
        public Config rotateShape ( RotateVoxelShape shape ) {
            if ( shape != null ) this.rotateShape = shape ;
            return this ;
        } ;
        // 遮挡控制，会被properties覆盖
        public Config noOcclusion ( ) {
            this.properties.noOcclusion( ) ;
            return this ;
        } ;
        public Config noOcclusion ( boolean val ) {
            return val ? this.noOcclusion( ) : this ;
        } ;
        // 设置属性
        public Config properties ( Properties p ) {
            if ( p != null ) this.properties = p ;
            return this ;
        } ;
    } ;

    protected final double[] sitPos ; // 原始坐标
    protected final RotateVoxelShape.Build rotateShape ;
    protected final Direction defaultDirection ;

    public Chair( Config config ) {
        super( config.properties ) ;
        this.registerDefaultState( this.stateDefinition.any( )
                .setValue( FACING, this.getDefaultDirection( ) )
        ) ;
        this.sitPos = config.sitPos.clone( ) ;
        this.rotateShape = config.rotateShape.build( this.getDefaultDirection( ) ) ;
        this.defaultDirection = config.defaultDirection ;
    }

    public Chair( ) {
        this( new Config( ).chairHeight( 0.5d ) ) ;
    }

    public Direction getDefaultDirection ( ) {
        return DEF_DIRECTION ;
    } ;

    public void setSitPos ( double x, double y, double z ) {
        this.sitPos[0] = x ;
        this.sitPos[1] = y ;
        this.sitPos[2] = z ;
    } ;

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context ) {
        return this.rotateShape.get( state.getValue( FACING ) ) ;
    }

    @Override
    public ActionResultType use( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result ) {
        if ( !world.isClientSide( ) ) {
            Direction facing = state.getValue( FACING ) ;
            RotatePos.RotateAngle ra = RotatePos.RotateAngle.get( ( facing.get2DDataValue( ) -DEF_DIRECTION.get2DDataValue( ) ) & 3 ) ;
            // 这里写反是因为奇奇怪怪的mojang的奇奇怪怪的模型坐标
            double[] rotate = RotatePos.rotate( this.sitPos[2], this.sitPos[0], 0.5f, ra ) ;
            // 没有生物坐下时
            if ( !LivingEntitySit.isLivingEntitySittingOnPos( world, pos ) ) {
                LivingEntitySit.sitToChair( player, pos.getX( ) +rotate[0], pos.getY( ) +this.sitPos[1], pos.getZ( ) +rotate[1], false ) ;
            } else {
                return ActionResultType.FAIL ;
            } ;
        }
        return ActionResultType.SUCCESS ;
    }

    @Override
    protected void createBlockStateDefinition( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( FACING ) ;
        super.createBlockStateDefinition( builder ) ;
    }

    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        Direction hozd = context.getHorizontalDirection( ) ;
        PlayerEntity player = context.getPlayer( ) ;
        // 蹲下时放一个当前方向的，否则放一个朝向玩家的
        if ( player != null ) {
            // hozd = player.getPose( ).equals( Pose.CROUCHING ) ? hozd :  ;
            hozd = hozd.getOpposite( ) ;
        } ;
        return this.defaultBlockState( ).setValue( FACING, hozd ) ;
    }

}
