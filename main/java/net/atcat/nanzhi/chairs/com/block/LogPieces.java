package net.atcat.nanzhi.chairs.com.block;

import net.atcat.nanzhi.chairs.com.RotateVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;

public class LogPieces extends Chair {
    public LogPieces( ) {
       super( new Config( )
               .sitPos( 11d / 16d, 6d / 16d, 8d / 16d )
               .rotateShape( new RotateVoxelShape( )
                       .or( 0, 0, 8, 16, 6, 14 )
               )
               .properties( Properties
                       .of( Material.WOOD, MaterialColor.WOOD )
                       .strength( 2.0f )
                       .sound( SoundType.WOOD )
               )
       ) ;
    } ;
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        Direction hozd = context.getHorizontalDirection( ) ;
        // 一个永远跟玩家方向同步的方块
        // PlayerEntity player = context.getPlayer( ) ;
        /*
        if ( player != null ) {
            hozd = player.getPose( ).equals( Pose.CROUCHING ) ? hozd.getOpposite( ) : hozd ;
        } ;
         */
        return this.defaultBlockState( ).setValue( FACING, hozd ) ;
    }
}
