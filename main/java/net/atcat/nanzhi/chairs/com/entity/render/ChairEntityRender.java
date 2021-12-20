package net.atcat.nanzhi.chairs.com.entity.render;

import net.atcat.nanzhi.chairs.NZChairsEntity;
import net.atcat.nanzhi.chairs.com.entity.ChairEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ChairEntityRender extends EntityRenderer<ChairEntity> {

    protected ChairEntityRender(EntityRendererManager p_i46179_1_ ) {
        super( p_i46179_1_ );
    }

    @SubscribeEvent
    public static void onClientSetUpEvent( FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler( NZChairsEntity.ride_entity, (EntityRendererManager manager ) -> {
            return new ChairEntityRender( manager ) ;
        } ) ;
    }

    @Override
    public ResourceLocation getTextureLocation(ChairEntity p_110775_1_) {
        return null;
    }

}
