package net.atcat.nanzhi.chairs.com.entity;

import net.atcat.nanzhi.chairs.NZChairsEffect;
import net.atcat.nanzhi.chairs.NZChairsEntity;
import net.atcat.nanzhi.chairs.com.block.AssCancerChair;
import net.atcat.nanzhi.chairs.com.func.LivingEntitySit;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ChairEntity extends Entity {

    public static final float HEIGHT = 0.02f ;
    public static final float WIDTH = 0.4f ;

    //不需要保存
    private int checkTick = 0 ;

    // 快速建立，坐标自己设置
    public ChairEntity( EntityType<?> entityTypeIn, World world ) {
        super( entityTypeIn, world ) ;
        this.setInvulnerable( true ) ;
    }

    // 传入世界，x，y，z
    public ChairEntity( World world, double x, double y, double z ) {
        this( NZChairsEntity.ride_entity, world ) ;
        this.setPos( x, y, z );
        this.xo = x ;
        this.yo = y ;
        this.zo = z ;
    }

    @Override
    public void tick( ) {
        // 不处理客户端
        if ( !this.level.isClientSide ) {
            // 没有乘客则销毁自身
            if ( this.getPassengers( ).size( ) == 0 ) {
                this.remove( ) ;
            } else if ( this.checkTick == 0 ) {
                // 玩家坐着核废料时，给一个屁屁癌
                if ( this.level.getBlockState( new BlockPos( this.position( ).x( ), this.position( ).y( ) -0.125, this.position( ).z( ) ) ).getBlock( ) instanceof AssCancerChair) {
                    this.getPassengers( ).forEach( entity -> {
                        if ( entity instanceof LivingEntity) {
                            ( (LivingEntity) entity ).addEffect( new EffectInstance( NZChairsEffect.ass_cancer, 3600, 0, false, true ) ) ;
                        }
                    } ) ;
                } ;
                // 玩家姿势不对则销毁自身
                if ( !LivingEntitySit.isValidityLocation( this.level, this.position( ), this.getPassengers( ).get( 0 ).getPose( ) ) ) {
                    this.remove( ) ;
                } ;
            } ;
            this.checkTick ++ ;
            this.checkTick &= 15 ;
        }
        super.tick( ) ;
    }

    // public double getMyRidingOffset() { return 0.0D; }
    // 获得偏移量
    public double getPassengersRidingOffset( ) {
        return (double)this.getEyeHeight( ) -0.25f;
    }

    @Override
    protected float getEyeHeight( Pose pose, EntitySize size ) {
        return size.height ;
    }

    @Override
    protected void defineSynchedData( ) { }

    @Override
    protected void readAdditionalSaveData( CompoundNBT compoundNBT ) { }

    @Override
    protected void addAdditionalSaveData( CompoundNBT compoundNBT ) { }

    @Override
    public IPacket<?> getAddEntityPacket( ) {
        return NetworkHooks.getEntitySpawningPacket( this ) ;
    }
}
