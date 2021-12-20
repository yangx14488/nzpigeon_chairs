package net.atcat.nanzhi.chairs.com.func;

public class RotatePos {

    public enum RotateAngle {
        R0(0), R90(1), R180(2), R270(3);
        public final int id  ;
        RotateAngle( int id ) {
            this.id = id ;
        }
        public static RotateAngle get ( int id ) {
           switch ( id ) {
               case 1:
                   return R90 ;
               case 2:
                   return R180 ;
               case 3:
                   return R270 ;
               default:
                   return R0 ;
           }
        } ;
    } ;

    public static double[] rotate ( double x, double z, double center, RotateAngle rotate ) {
        double[] ret = new double[2] ;
        double lx = x -center ;
        double lz = z -center ;
        switch ( rotate ) {
            case R90 :
                ret[0] = -lz ;
                ret[1] = lx ;
                break ;
            case R180 :
                ret[0] = -lx ;
                ret[1] = -lz ;
                break ;
            case R270 :
                ret[0] = lz ;
                ret[1] = -lx ;
                break ;
            case R0 :
                ret[0] = lx ;
                ret[1] = lz ;
                break ;
        } ;
        ret[0] += center ;
        ret[1] += center ;
        return ret ;
    } ;
    public static float[] rotate ( float x, float z, float center, RotateAngle rotate ) {
        float[] ret = new float[2] ;
        float lx = x -center ;
        float lz = z -center ;
        switch ( rotate ) {
            case R90 :
                ret[0] = -lz ;
                ret[1] = lx ;
                break ;
            case R180 :
                ret[0] = -lx ;
                ret[1] = -lz ;
                break ;
            case R270 :
                ret[0] = lz ;
                ret[1] = -lx ;
                break ;
            case R0 :
                ret[0] = lx ;
                ret[1] = lz ;
                break ;
        } ;
        ret[0] += center ;
        ret[1] += center ;
        return ret ;
    } ;
    public static int[] rotate ( int x, int z, int center, RotateAngle rotate ) {
        int[] ret = new int[2] ;
        int lx = x -center ;
        int lz = z -center ;
        switch ( rotate ) {
            case R90 :
                ret[0] = -lz ;
                ret[1] = lx ;
                break ;
            case R180 :
                ret[0] = -lx ;
                ret[1] = -lz ;
                break ;
            case R270 :
                ret[0] = lz ;
                ret[1] = -lx ;
                break ;
            case R0 :
                ret[0] = lx ;
                ret[1] = lz ;
                break ;
        } ;
        ret[0] += center ;
        ret[1] += center ;
        return ret ;
    } ;

}
