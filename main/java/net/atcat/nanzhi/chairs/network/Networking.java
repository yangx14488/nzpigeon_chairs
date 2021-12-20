package net.atcat.nanzhi.chairs.network;

import net.atcat.nanzhi.chairs.NZChairs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class Networking {
    public static SimpleChannel INSTANCE ;
    public static final String VER = "1.0";
    private static int ID = 0 ;

    public static int nextID( ) {
        return ID++;
    }

    public static void registerMessage( ) {
        INSTANCE = NetworkRegistry.newSimpleChannel (
                new ResourceLocation( NZChairs.ID, "sit_com") ,
                ( ) -> VER,
                ver -> ver.equals( VER ),
                ver -> ver.equals( VER )
        ) ;
        INSTANCE.messageBuilder( DataPack.class, nextID( ) )
                .encoder( DataPack::toBytes )
                .decoder( DataPack::new )
                .consumer( DataPack::handler )
                .add( ) ;
    }

    @SubscribeEvent
    public static void onCommonSetup( FMLCommonSetupEvent event ) {
        event.enqueueWork( Networking::registerMessage ) ;
    } ;

}
