package net.atcat.nanzhi.chairs.com.effect;

import net.atcat.nanzhi.chairs.obj.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class AssCancer extends Effect {

    protected final List<ItemStack> curativeItems = new ArrayList<>( ) ;

    public AssCancer( ) {
        super( EffectType.HARMFUL, Color.encode( 184,150,129 ) ) ;
    }

    // 覆盖原有治疗物品
    @Override
    public List<ItemStack> getCurativeItems( ) {
        return this.curativeItems ;
    }

}