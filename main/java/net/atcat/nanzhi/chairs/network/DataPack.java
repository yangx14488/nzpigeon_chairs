package net.atcat.nanzhi.chairs.network;

import net.atcat.nanzhi.chairs.com.func.LivingEntitySit;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DataPack {

    private byte data = 0 ;

    public DataPack( ) {
        this.data = 0 ;
    }

    public DataPack( PacketBuffer buffer ) {
        try {
            this.data = buffer.readByte( ) ;
        } catch ( Exception ignored ) {
            // 防错误数据
        } ;
    }

    public void toBytes( PacketBuffer buf ) {
        buf.writeByte( data ) ;
    }

    public void handler( Supplier<NetworkEvent.Context> ctx ) {
        ctx.get( ).enqueueWork( ( ) -> {
            ServerPlayerEntity player = ctx.get( ).getSender( ) ;
            if ( player != null ) {
                LivingEntitySit.dataPackSit( player ) ;
            } ;
        } ) ;
        ctx.get( ).setPacketHandled( true ) ;
    }

}
