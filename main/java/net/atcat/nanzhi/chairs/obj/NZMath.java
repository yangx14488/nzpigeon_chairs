package net.atcat.nanzhi.chairs.obj;

public class NZMath {

    public static int limit( int min, int val, int max ) {
        return Math.max( min, Math.min( val, max ) ) ;
    }

    public static float limit( float min, float val, float max ) {
        return Math.max( min, Math.min( val, max ) ) ;
    }

    public static long limit( long min, long val, long max ) {
        return Math.max( min, Math.min( val, max ) ) ;
    }

    public static double limit( double min, double val, double max ) {
        return Math.max( min, Math.min( val, max ) ) ;
    }

    public static int getFirstBit( int val ) {
        int i = 31 ;
        while ( i --> 0 ) if ( ( val & ( 1 << i ) ) != 0 ) break ;
        return i +1 ;
    }

    public static int getFirstBit( long val ) {
        int i = 63 ;
        while ( i --> 0 ) if ( ( val & ( 1L << i ) ) != 0 ) break ;
        return i +1 ;
    }

    public static int setBitToInt ( int data, int length, int offset, int val ) { // 原始数据，单个数据长度，偏移量，设置值
        int ofs = length *offset ;
        int bit = ( 1 << length ) -1 ;
        int ret = data ;
        ret &= ~( bit << ofs ) ; // 将要设置的位写为0
        ret |= ( NZMath.limit( 0, val, bit ) << ofs ) ; // 写入
        return ret ;
    } ;

    public static int getBitFromInt ( int data, int length, int offset ) { // 读取
        return ( data >>> ( length *offset ) ) & ( ( 1 << length ) -1 ) ;
    } ;

}