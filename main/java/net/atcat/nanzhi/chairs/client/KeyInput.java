package net.atcat.nanzhi.chairs.client;

import net.atcat.nanzhi.chairs.com.func.LivingEntitySit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber( value = Dist.CLIENT )
public class KeyInput {

    public static final KeyBinding SIT_KEY = new KeyBinding("key.sit",
            KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.categories.movement" ) ;

    @SubscribeEvent
    public static void onKeyDown ( InputEvent.KeyInputEvent event ) {
        Minecraft minecraft = Minecraft.getInstance( ) ;
        ClientPlayerEntity player = minecraft.player ;
        if ( player != null ) {
            if ( !SIT_KEY.isUnbound( ) && SIT_KEY.isDown( ) ) {
                if ( player.isPassenger( ) ) {
                    LivingEntitySit.dataPackSit( player ) ; // 解除
                } else {
                    LivingEntitySit.dataPackSit( player ) ; // 乘坐
                } ;
            } ;
        } ;
    }

}