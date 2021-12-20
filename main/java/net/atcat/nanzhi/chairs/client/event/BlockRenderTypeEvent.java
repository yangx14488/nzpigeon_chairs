package net.atcat.nanzhi.chairs.client.event;

import net.atcat.nanzhi.chairs.NZChairs;
import net.atcat.nanzhi.chairs.com.block.in.BlockTypeRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn( Dist.CLIENT )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class BlockRenderTypeEvent {
    @SubscribeEvent
    static void onRenderTypeSetup( FMLClientSetupEvent event ) {
        event.enqueueWork( ( ) -> {
            NZChairs.REG_GROUP.forEach( ForgeRegistries.BLOCKS, entity -> {
                Block block = entity.get( ) ;
                RenderType type = null ;
                if ( block instanceof BlockTypeRender ) {
                    switch ( ( (BlockTypeRender) block ).getRenderType( ) ) {
                        case solid:
                            type = RenderType.solid( ) ;
                            break;
                        case cutout:
                            type = RenderType.cutout( ) ;
                            break;
                        case translucent:
                            type = RenderType.translucent( ) ;
                            break;
                        case cutout_mipped:
                            type = RenderType.cutoutMipped( ) ;
                            break;
                    }
                }
                if ( type != null ) {
                    RenderTypeLookup.setRenderLayer( block, type ) ;
                }
            } ) ;
        } ) ;
    }

    public static void loop ( DeferredRegister<Block> reg ) {

    } ;

}
