package net.atcat.nanzhi.chairs.com.block;

import net.atcat.nanzhi.chairs.MSG;
import net.atcat.nanzhi.chairs.com.RotateVoxelShape;
import net.atcat.nanzhi.chairs.com.func.LivingEntitySit;
import net.atcat.nanzhi.chairs.com.func.RotatePos;
import net.atcat.nanzhi.chairs.com.func.SendTitleToPlayer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ButtChair extends Chair {

    public static final RotateVoxelShape ROTATE_VOXEL_SHAPE = new RotateVoxelShape( )
            .or( 1, 0, 1, 3, 8, 3 )
            .or( 13, 0, 1, 15, 8, 3 )
            .or( 1, 0, 13, 3, 11, 15 )
            .or( 13, 0, 13, 15, 11, 15 )
            .or( 1, 8, 1, 15, 10, 13 )
            .or( 1, 11, 13, 15, 23, 15 )
            .or( 1, 13, 3, 3, 14.5, 13 )
            .or( 13, 13, 3, 15, 14.5, 13 )
            .or( 8.2, 10, 5, 12.2, 11, 10 )
            .or( 3.8, 10, 5, 7.8, 11, 10 )
            .or( 7.5, 10, 5.1, 8.5, 10.5, 9.9 )
            ;
    public static final Properties PROPERTIES = AbstractBlock.Properties
            .of( Material.WOOD, MaterialColor.WOOD )
            .strength( 2.0f )
            .sound( SoundType.WOOD ) ;

    public ButtChair( ) {
       super( new Chair.Config( ).sitPos( 7d/16d, 11d/16d, 8d/16d ).rotateShape( ROTATE_VOXEL_SHAPE ).properties( PROPERTIES ) ) ;
    } ;

    @Override
    public ActionResultType use( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result ) {
        ActionResultType resultType = super.use( state, world, pos, player, hand, result ) ;
        if ( resultType.equals( ActionResultType.SUCCESS ) ) {
            if ( world.isClientSide( ) ) // 客户端专用
                player.displayClientMessage( MSG.comfortable_butt.get( ), false ) ;
        }
        return resultType ;
    }

}
