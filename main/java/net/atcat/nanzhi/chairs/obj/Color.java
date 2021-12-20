package net.atcat.nanzhi.chairs.obj;

public class Color {
    public int rgb = 0 ;
    public int r = 0 ;
    public int g = 0 ;
    public int b = 0 ;
    public Color( int r, int g, int b ) {
        this.r = Math.max( 0, Math.min( r, 255  ) ) ;
        this.g = Math.max( 0, Math.min( g, 255  ) ) ;
        this.b = Math.max( 0, Math.min( b, 255  ) ) ;
        this.rgb = ( this.r << 16 ) + ( this.g << 8 ) + this.b ;
    } ;
    public Color( int rgb ) {
        this.rgb = Math.max( 0, Math.min( rgb, 16777215 ) ) ;
        this.r = this.rgb >> 16 ;
        this.g = ( this.rgb >> 8 ) & 255 ;
        this.b = this.rgb & 255 ;
    } ;
    public static int encode ( int r, int g, int b ) {
        return ( Math.max( 0, Math.min( r, 255  ) ) << 16 ) + ( Math.max( 0, Math.min( g, 255  ) ) << 8 ) + Math.max( 0, Math.min( b, 255 ) ) ;
    } ;
    public static Color decode ( int rgb ) {
        return new Color( rgb ) ;
    } ;
}
