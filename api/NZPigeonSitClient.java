package net.atcat.nanzhi.im3;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class NZPigeonSitClient {

    @Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
    public static class SitEntityRender extends EntityRenderer<NZPigeonSit.SitEntity> {

        protected SitEntityRender( EntityRendererManager manager) {
            super( manager ) ;
        }

        @SubscribeEvent
        public static void onClientSetUpEvent( FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler( NZPigeonSit.SIT_ENTITY, ( EntityRendererManager manager ) -> {
                return new SitEntityRender( manager ) ;
            } ) ;
        }

        @Override
        public ResourceLocation getTextureLocation( NZPigeonSit.SitEntity entity ) {
            return null ;
        }

    }

} ;