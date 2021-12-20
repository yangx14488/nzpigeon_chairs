package net.atcat.nanzhi.chairs.jsonPack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObjectReader {

    // 参数定型
    public static class Reader <T> {
        public final Function<Object,T> reader ;

        /**
         *
         * @param reader   lambda表达式，传入一个没有类型的对象，返回一个确定的对象或null。
         */
        public Reader ( Function<Object,T> reader ) {
            this.reader = reader ;
        } ;
        /**
         *
         * @param val    需要确定类型的值
         * @return       通过阅读器获得的值
         */
        @Nullable
        public T getValue ( Object val ) {
            return this.reader.apply( val ) ;
        }
        /**
         *
         * @param val       需要确定类型的值
         * @param callback  回调（不是当前所需的类型时不会回调）
         */
        public void getValue ( Object val, Consumer<T> callback ) {
            T data = this.reader.apply( val ) ;
            if ( data != null ) {
                callback.accept( data ) ;
            } ;
        }
    } ;

    // 坐标读取
    public static Reader<List<Number>> posReader = new Reader<>( obj -> {
        List<Number> ls = listReader( obj, Number.class, 3 ) ;
        if ( ls != null ) {
            return ls ;
        } ;
        return null ;
    } ) ;
    // 碰撞箱读取
    public static Reader<List<List<Number>>> shapeReader = new Reader<>( obj -> {
        List<?> ls = listReader( obj, List.class, -1 ) ;
        if ( ls != null ) {
            List<List<Number>> ret = new ArrayList<>( ) ;
            for ( Object l : ls ) {
                List<Number> data = listReader( l, Number.class, 6 ) ;
                if ( data == null ) {
                    ret = null;
                    break;
                } else {
                    ret.add( data ) ;
                } ;
            }
            return ret;
        }
        return null ;
    } ) ;
    // 布尔读取
    public static Reader<Boolean> boolReader = new Reader<>( obj -> ( obj instanceof Boolean ) ? (Boolean) obj : null ) ;

    /** 用来将一个 Object 读取为集合的函数
     *
     * @param obj     需要读取的对象
     * @param cls     集合中的 目标类型 的类
     * @param <T>     目标类型
     * @return        返回一个符合规则的集合或者 null
     */
    @Nullable
    public static <T> List<T> listReader( Object obj, Class<T> cls ) {
        return listReader( obj, cls, -1 ) ;
    } ;
    /** 用来将一个 Object 读取为集合的函数
     *
     * @param obj     需要读取的对象
     * @param cls     集合中的 目标类型 的类
     * @param length  集合长度，小于1时，默认无长度限制，长度不匹配时视为不符合规则
     * @param <T>     目标类型
     * @return        返回一个符合规则的集合或者 null
     */
    @Nullable
    public static <T> List<T> listReader( Object obj, Class<T> cls, int length ) {
        List<T> ret = null ;
        if ( obj instanceof List ) { // 校验
            List<?> data = (List<?>) obj ;
            if ( length <= 0 || data.size( ) == length ) { // 检查长度是否符合要求
                ret = new ArrayList<>( ) ; // 声明实例
                for ( Object t : data )
                    if ( cls.isInstance( t ) ) {
                        ret.add( (T) t ) ;
                    } else {
                        JSONObj.LogPrinter.mismatchedClass( cls, t ) ;
                        break ; // 中止
                    } ;
                if ( length > 0 && ret.size( ) != length ) { // 长度二次检查
                    JSONObj.LogPrinter.mismatchedLength( length, ret.size( ) ) ;
                    ret = null; // 销毁
                }
            } else {
                JSONObj.LogPrinter.mismatchedLength( length, data.size( ) ) ;
            } ;
        } else {
            JSONObj.LogPrinter.mismatchedClass( List.class, obj ) ;
        } ;
        return ret ;
    } ;


}
