package net.atcat.nanzhi.chairs.com.block;

import net.atcat.nanzhi.chairs.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.chairs.jsonPack.PackObj;

public class PackChair extends Chair implements BlockTypeRender {
    public PackChair(PackObj.Entry entry ) {
        super( new Config( )
                .sitPos( entry.pos )
                .noOcclusion( entry.noOcclusion )
                .rotateShape( entry.rShape )
        ) ;
    }

    @Override
    public RenderTypes getRenderType( ) {
        return RenderTypes.cutout_mipped ;
    }

}
