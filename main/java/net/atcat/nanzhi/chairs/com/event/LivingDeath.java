package net.atcat.nanzhi.chairs.com.event;

import net.atcat.nanzhi.chairs.NZChairs;
import net.atcat.nanzhi.chairs.NZChairsBlocks;
import net.atcat.nanzhi.chairs.NZChairsEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( modid = NZChairs.ID, bus = Mod.EventBusSubscriber.Bus.FORGE )
public class LivingDeath {

    @SubscribeEvent( priority = EventPriority.LOWEST )
    public static void onLivingDeath( LivingDeathEvent event ) {
        LivingEntity entity = event.getEntityLiving( ) ;
        if ( entity != null && !entity.level.isClientSide( ) && entity.hasEffect( NZChairsEffect.ass_cancer ) ) {
            ItemEntity item = new ItemEntity( entity.level, entity.position( ).x( ), entity.position( ).y( ), entity.position().z( ) ) ;
            item.spawnAtLocation( new ItemStack ( NZChairsBlocks.butt_chair, 1 ), entity.getEyeHeight( ) ) ;
        } ;
    }  ;

}
