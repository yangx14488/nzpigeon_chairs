package net.atcat.nanzhi.chairs.com.event;

import net.atcat.nanzhi.chairs.JSONDataPack;
import net.atcat.nanzhi.chairs.MSG;
import net.atcat.nanzhi.chairs.NZChairs;
import net.atcat.nanzhi.chairs.com.func.RotatePos;
import net.atcat.nanzhi.chairs.com.func.SendTitleToPlayer;
import net.atcat.nanzhi.chairs.com.func.LivingEntitySit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;

@Mod.EventBusSubscriber( modid = NZChairs.ID, bus = Mod.EventBusSubscriber.Bus.FORGE )
public class RightClickEvent {

    // 右键箱子
    @SubscribeEvent( priority = EventPriority.HIGHEST )
    public static void clickChest ( PlayerInteractEvent.RightClickBlock event ) {
        PlayerEntity player = event.getPlayer( ) ;
        // 服务器
        Block block = event.getWorld( ).getBlockState( event.getPos( ) ).getBlock( ) ;
        // 检测箱子
        if ( block instanceof ChestBlock ) {
            if ( LivingEntitySit.isLivingEntitySittingOnPos( event.getWorld( ), event.getPos( ) ) ) {
                if ( event.getSide( ).isServer( ) ) // 仅服务端拒绝，这样子有动画
                    event.setUseBlock( Result.DENY ) ; // 拒绝方块调用
                if ( !player.getPose( ).equals( Pose.CROUCHING ) ) { // 双端拒绝非蹲下的请求
                    event.setUseItem( Result.DENY ) ;
                } ;
                // event.setResult( Result.ALLOW ) ;
            } ;
        } ;
    } ;

    @SubscribeEvent( priority = EventPriority.LOWEST )
    public static void onPlayerClickBlock ( PlayerInteractEvent.RightClickBlock event ) {
        PlayerEntity player = event.getPlayer( ) ;
        // 服务器且玩家没蹲下
        if ( !player.getPose( ).equals( Pose.CROUCHING ) ) {
            Double[] pos ;
            BlockState state = event.getWorld( ).getBlockState( event.getPos( ) ) ;
            Block block = event.getWorld( ).getBlockState( event.getPos( ) ).getBlock( ) ;
            ResourceLocation regName = block.getRegistryName( ) ;
            // 检查
            if ( JSONDataPack.chairBlockList != null && regName != null ) {
                pos = JSONDataPack.chairBlockList.idList.get( regName.toString( ) ) ;
                if ( pos != null ) {
                    double[] rotate = new double[] {
                            pos[2],
                            pos[0]
                    } ;
                    if ( state.hasProperty( BlockStateProperties.FACING ) ) {
                        RotatePos.RotateAngle ra = RotatePos.RotateAngle.get( ( state.getValue( BlockStateProperties.FACING ).get2DDataValue( ) - Direction.NORTH.get2DDataValue( ) ) & 3 ) ;
                        rotate = RotatePos.rotate( rotate[0], rotate[1], 0.5f, ra ) ;
                    } ;
                    if ( event.getSide( ).isServer( ) ) {
                        LivingEntitySit.sitToChair( player, event.getPos().getX() +rotate[0], event.getPos().getY() +pos[1], event.getPos().getZ() +rotate[1], false ) ;
                    }
                    event.setCanceled( true ) ;
                } ;
            } ;
        } ;
    }  ;

}
