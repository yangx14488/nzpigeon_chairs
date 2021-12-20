package net.atcat.nanzhi.chairs.jsonPack;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class ChairBlocksObj extends JSONObj {

    // 原始数据
    public static class UntreatedData {
        public final Map<String,Map<String,?>> chairs_id ;
        public UntreatedData( Map<String,Map<String,?>> ci, Map<String,Map<String,?>> ct ) {
            this.chairs_id = ci ;
        } ;
    } ;

    public static Double[] getPos (Map<String,?> map ) {
        Double[] ret = new Double[3] ;
        for ( Map.Entry<String,?> entry : map.entrySet( ) ) {
            String key = entry.getKey( ) ;
            Object val = entry.getValue( ) ;
            if ( "sit_pos".equals( key ) ) { // 坐标读取
                ObjectReader.posReader.getValue( val, data -> {
                    for ( int i = 0; i < 3; i++ )
                        ret[i] = ( data.get( i ) ).doubleValue( ) ;
                } ) ;
            }
            ;
        } ;
        return ret ;
    } ;

    // 两个列表
    public final Map<String,Double[]> idList = new HashMap<>() ;

    public ChairBlocksObj ( UntreatedData dc ) {
        // 添加id
        dc.chairs_id.forEach( ( id, map ) -> {
            this.idList.put( id, getPos( map ) ) ;
        } ) ;
    } ;

    @Nullable
    public static ChairBlocksObj getObjectFromJSONFile(String path ) {
        UntreatedData dc = getObjectFromJSONFile( path, UntreatedData.class ) ;
        return dc != null ? new ChairBlocksObj( dc ) : null ;
    }

}
