package net.atcat.nanzhi.chairs.jsonPack;

import net.atcat.nanzhi.chairs.com.RotateVoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PackObj extends JSONObj {

    /** 用来存放不确定参数类型的原始数据
     * @apiNote blocks  原始映射集合
     */
    public static class UntreatedData {
        public final Map<String, Map<String,?>> blocks ;
        public UntreatedData( Map<String,Map<String,?>> blocks ) {
            this.blocks = blocks ;
        } ;
    } ;
    /** 入口类，存储了构建一个椅子方块所需要的一切信息
     * @apiNote id      存放着构建时使用的id
     * @apiNote pos     定长数组，表明玩家点击时会坐下的位置（x，y，z）
     * @apiNote rShape  可旋转体积，越写越想给mojiang一拳
     */
    public static class Entry {
        public final String id ;
        public final double[] pos = new double[] { 0.5d, 0.5d, 0.5d } ;
        public boolean noOcclusion = false ;
        public boolean assCancer = false ;
        public RotateVoxelShape rShape = new RotateVoxelShape( ) ;
        /** 构建函数
         *
         * @param id   方块构建的id
         * @param map  需要读取的对象，结构类似 { "sitpos" : [ 0,0,0 ], "shapebox" : [ [ 0,0,0,0,0,0], ... ] }
         */
        public Entry ( String id, Map<String,?> map ) {
            this.id = id ;
            for ( Map.Entry<String,?> entry : map.entrySet( ) ) {
                String key = entry.getKey( ) ;
                Object val = entry.getValue( ) ;
                switch ( key ) {
                    case "sit_pos" : // 坐标读取
                        ObjectReader.posReader.getValue( val, data -> {
                            // 写入坐下的坐标
                            for ( int i = 0 ; i < 3 ; i ++ )
                                this.pos[i] = ( data.get( i ) ).doubleValue( ) ;
                        } ) ;
                        break ;
                    case "shape_box" : // 读取碰撞箱
                        ObjectReader.shapeReader.getValue( val, data -> {
                            data.forEach( list -> {
                                // 数据转录
                                double[] pos = new double[6] ;
                                for ( int i = 0 ; i < 6 ; i++ )
                                    pos[i] = list.get( i ).doubleValue( ) ;
                                // 碰撞箱赋值
                                rShape.or( pos[0], pos[1], pos[2], pos[3], pos[4], pos[5] ) ;
                            } ) ;
                        } ) ;
                        break ;
                    case "no_occlusion" : // 读取遮挡开关
                        ObjectReader.boolReader.getValue( val, data -> {
                            this.noOcclusion = data ;
                        } ) ;
                        break ;
                    case "ass_cancer" : // 屁屁癌
                        ObjectReader.boolReader.getValue( val, data -> {
                            this.assCancer = data ;
                        } ) ;
                        break ;
                } ;
            } ;
        } ;
    } ;

    // 方块集合
    public final List<Entry> blocks = new ArrayList<>() ;
    // 实例方法
    public PackObj( UntreatedData data ) {
        // 存入方块
        data.blocks.forEach( ( id, map ) -> this.blocks.add( new Entry( id, map ) ) ) ;
    } ;
    // 从路径生成一个当前对象
    @Nullable
    public static PackObj getObjectFromJSONFile(String path ) {
        UntreatedData data = getObjectFromJSONFile( path, UntreatedData.class ) ;
        return data != null ? new PackObj( data ) : null ;
    }


}
