package net.atcat.nanzhi.chairs;

import net.atcat.nanzhi.chairs.com.entity.ChairEntity;
import net.minecraft.entity.EntityType;

public class NZChairsEntity {

    public static void load ( ) { } ;

    public static final EntityType<ChairEntity> ride_entity = NZChairs.REG.register( "ride_entity", (a, b ) -> { return new ChairEntity( a, b ) ; }, ChairEntity.WIDTH, ChairEntity.HEIGHT ) ;

}
