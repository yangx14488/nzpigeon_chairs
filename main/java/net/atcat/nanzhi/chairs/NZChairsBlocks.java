package net.atcat.nanzhi.chairs;

import net.atcat.nanzhi.chairs.com.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class NZChairsBlocks {

    public static void load ( ) { } ;

    // 木段们
    public static Block acacia_log_pieces = NZChairs.REG.register( "acacia_log_pieces", new LogPieces( ), ItemGroup.TAB_DECORATIONS ) ;
    public static Block birch_log_pieces = NZChairs.REG.register( "birch_log_pieces", new LogPieces( ), ItemGroup.TAB_DECORATIONS ) ;
    public static Block dark_oak_log_pieces = NZChairs.REG.register( "dark_oak_log_pieces", new LogPieces( ), ItemGroup.TAB_DECORATIONS ) ;
    public static Block jungle_log_pieces = NZChairs.REG.register( "jungle_log_pieces", new LogPieces( ), ItemGroup.TAB_DECORATIONS ) ;
    public static Block oak_log_pieces = NZChairs.REG.register( "oak_log_pieces", new LogPieces( ), ItemGroup.TAB_DECORATIONS ) ;
    public static Block spruce_log_pieces = NZChairs.REG.register( "spruce_log_pieces", new LogPieces( ), ItemGroup.TAB_DECORATIONS ) ;

    // 屁屁椅
    public static Block butt_chair = NZChairs.REG.register( "butt_chair", new ButtChair( ), ItemGroup.TAB_DECORATIONS ) ;

}
