package net.atcat.nanzhi.chairs.com.block.in;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface BlockTypeRender {

    /** 用于设置渲染类型的方法
     * @return 渲染类型
     * @see RenderTypes   - 可用列表
     *
     * 用法：让方块导入这个类，并定义getRenderType方法，方块在注册时则会自动设置渲染类型
     **/
    @OnlyIn( Dist.CLIENT )
    RenderTypes getRenderType( ) ;

    /**
     * translucent: 半透明
     * solid: 不透明
     * cutout: 二值透明
     * cutout_mipped: 二值烘焙透明
     **/
    enum RenderTypes {
        translucent, solid, cutout, cutout_mipped
    }

}
