package net.atcat.nanzhi.chairs.com.func;

import net.atcat.nanzhi.chairs.MSG;
import net.atcat.nanzhi.chairs.NZChairs;
import net.atcat.nanzhi.chairs.NZChairsEffect;
import net.atcat.nanzhi.chairs.com.entity.ChairEntity;
import net.atcat.nanzhi.chairs.network.DataPack;
import net.atcat.nanzhi.chairs.network.Networking;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/** 让实体生物坐下的方法类
 *
 * 别去访问 dataPackSit 方法，这个方法是数据包用的
 * 不然可能出一些奇奇怪怪的bug
 *
 *       - 南织鸽子
 */
public class LivingEntitySit {

    // 检查坐标是否有生物坐着的函数，传入一个世界和坐标
    public static boolean isLivingEntitySittingOnPos( IWorld world, BlockPos pos ) {
        return isLivingEntitySittingOnPos( world, pos.getX( ), pos.getY( ), pos.getZ( ) ) ;
    }
    public static boolean isLivingEntitySittingOnPos( IWorld world, double posX, double posY, double posZ ) {
        List<ChairEntity> list = world.getEntitiesOfClass( ChairEntity.class, new AxisAlignedBB( posX +0.0625, posY, posZ +0.0625, posX +0.9375, posY +1.5, posZ +0.9375 ) ) ;
        return list.size( ) != 0 ;
    }
    /** 让生物坐在指定坐标
     *  预留ignoreCrowded是因为椅子方块不会检查，因为椅子方块自己会检查，并且不会提示
     *
     * @param entity   生物
     * @param pos      坐标
     * @param sendMessage   不检查
     * @return   椅子实体
     */
    @Nullable
    public static ChairEntity sitToChair ( LivingEntity entity, Vector3d pos, boolean sendMessage ) {
        return sitToChair( entity, pos.x( ), pos.y( ), pos.z( ), sendMessage ) ;
    } ;
    /** 让生物坐在指定坐标
     *
     * @param entity   生物
     * @param x   坐标X
     * @param y   坐标Y
     * @param z   坐标Z
     * @param sendMessage   不检查
     * @return   椅子实体
     */
    public static ChairEntity sitToChair ( LivingEntity entity, double x, double y, double z, boolean sendMessage ) {
        // 不处理尸体
        if ( !entity.isAlive( ) ) { return null ; } ;
        if ( entity.hasEffect( NZChairsEffect.ass_cancer ) ) {
            // 屁屁癌总是发送消息
            if ( entity instanceof PlayerEntity ) { SendTitleToPlayer.actionbar( (PlayerEntity) entity, MSG.ass_cancer.get( ) ) ; } ;
            return null ;
        }
        if ( isLivingEntitySittingOnPos( entity.level, x, y, z ) ) { // 有生物坐着
            // 通知
            if ( sendMessage && entity instanceof PlayerEntity ) { SendTitleToPlayer.actionbar( (PlayerEntity) entity, MSG.too_crowded.get( ) ) ; } ;
            return null ;
        } ;
        // 生成座椅
        ChairEntity chair = new ChairEntity( entity.level, x, y, z ) ;
        // 添加座椅
        entity.level.addFreshEntity( chair ) ;
        // 解除乘坐状态
        if ( entity.isPassenger( ) ) { entity.stopRiding( ) ; } ;
        // 让生物坐上去
        entity.startRiding( chair ) ;
        return chair ;
    } ;
    // 同上，只不过已经写好了一些参数，能直接坐在原地
    public static ChairEntity sitToChair ( LivingEntity entity ) {
        return sitToChair( entity, entity.position( ), true ) ;
    } ;

    /** 坐标合理性判断
     *
     * @param world    世界
     * @param pos      检测的位置，通常是乘客或者自身的位置
     * @param passengersPose   乘客的姿势
     * @return   是否通过校验
     */
    public static boolean isValidityLocation( IWorld world, Vector3d pos, Pose passengersPose ) {
        return isValidityLocation( world, new BlockPos( Math.floor( pos.x( ) ), Math.floor( pos.y( ) - 0.03 ), Math.floor( pos.z( ) ) ), passengersPose ) ;
    } ;
    /** 坐标合理性判断
     *
     * 同上，只不过pos换成了检测点
     */
    public static boolean isValidityLocation( IWorld world, BlockPos pos, Pose passengersPose ) {
        return !world.getBlockState( pos ).getMaterial( ).equals( Material.AIR ) && ( passengersPose.equals( Pose.STANDING ) || passengersPose.equals( Pose.CROUCHING ) ) ;
    } ;
    /** 坐标合理性判断
     *
     * 同上，只不过这个需要传入一个需要坐下的生物
     */
    public static boolean isValidityLocation( LivingEntity entity ) {
        return entity.isOnGround( ) && isValidityLocation( entity.level, entity.position( ), entity.getPose( ) ) ;
    } ;
    // 客户端用的方法，能让一个玩家从服务端坐下，会校验之后再把请求丢给服务器处理
    @OnlyIn( Dist.CLIENT )
    public static boolean dataPackSit( ClientPlayerEntity player ) {
        if ( player.isPassenger( ) || isValidityLocation( player ) ) {
            Networking.INSTANCE.sendToServer( new DataPack( ) ) ;
            return true ;
        } ;
        return false ;
    } ;
    // 服务端用的方法，能让一个玩家坐下，会在服务端再进行一次校验以防止数据不同步
    public static boolean dataPackSit( ServerPlayerEntity player ) {
        if ( player.isPassenger( ) ) {
            player.stopRiding( ) ;
            return true ;
        } else {
            if ( isValidityLocation( player ) ) { // 坐标合理性
                sitToChair( player ) ;
                return true ;
            } else {
                // 服务器的提示，客户端通常不会出现，除非数据不同步
                SendTitleToPlayer.actionbar( player, player.isOnGround( ) ? MSG.wrong_pose.get( ) : MSG.can_not_sit.get( ) ) ;
            } ;
        } ;
        return false ;
    } ;

}
