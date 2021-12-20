package net.atcat.nanzhi.chairs.com.func;

import net.atcat.nanzhi.chairs.MSG;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SendTitleToPlayer {

    private static void sendTitlePacket ( STitlePacket.Type type, PlayerEntity player, ITextComponent string ) {
        if ( player instanceof ServerPlayerEntity ) {
            ( (ServerPlayerEntity) player ).connection.send( new STitlePacket( type, string ) ) ;
        } ;
    } ;

    public static void actionbar ( PlayerEntity player, ITextComponent string ) {
        actionbar( player, string, false ) ;
    } ;

    // 不使用client开关时，消息会走服务器发送，否则会从客户端本地发送
    public static void actionbar ( PlayerEntity player, ITextComponent string, boolean client ) {
        if ( client ) {
            player.displayClientMessage( string, true ) ;
        } else {
            sendTitlePacket( STitlePacket.Type.ACTIONBAR, player, string ) ;
        } ;
    } ;

    public static void actionbar ( PlayerEntity player, String i18n, Object... args ) {
        sendTitlePacket( STitlePacket.Type.ACTIONBAR, player, new TranslationTextComponent( i18n, args ) ) ;
    } ;

    public static void title ( PlayerEntity player, ITextComponent string ) {
        sendTitlePacket( STitlePacket.Type.TITLE, player, string ) ;
    } ;

    public static void title ( PlayerEntity player, String i18n, Object... args ) {
        sendTitlePacket( STitlePacket.Type.TITLE, player, new TranslationTextComponent( i18n, args ) ) ;
    } ;

    public static void subtitle ( PlayerEntity player, ITextComponent string ) {
        sendTitlePacket( STitlePacket.Type.SUBTITLE, player, string ) ;
    } ;

    public static void subtitle ( PlayerEntity player, String i18n, Object... args ) {
        sendTitlePacket( STitlePacket.Type.SUBTITLE, player, new TranslationTextComponent( i18n, args ) ) ;
    } ;

    public static void time ( PlayerEntity player, int fadeInTime, int stayTimem, int fadeOutTime ) {
        if ( player instanceof ServerPlayerEntity ) {
            ( (ServerPlayerEntity) player ).connection.send( new STitlePacket( fadeInTime, stayTimem, fadeOutTime ) ) ;
        } ;
    } ;

}
