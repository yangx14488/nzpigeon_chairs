package net.atcat.nanzhi.chairs.com;

import net.atcat.nanzhi.chairs.com.func.RotatePos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape ;
import net.minecraft.util.math.shapes.VoxelShapes;


// 请注意，这里的旋转方向是以模型的北朝-x为基准做的碰撞箱
public class RotateVoxelShape {

    public static class Build {
        public final int offset ;
        public final VoxelShape[] shapeList ;
        public Build( int offset, VoxelShape[] shapeList ) {
            this.shapeList = shapeList ;
            this.offset = offset ;
        } ;
        public VoxelShape get ( Direction direction ) {
            return this.shapeList[ ( ( ( direction.get2DDataValue( ) +2 ) & 3 ) - offset) & 3 ] ;
        } ;
    } ;
    public static final VoxelShape[] BOX = new VoxelShape[ ] { VoxelShapes.block( ), VoxelShapes.block( ), VoxelShapes.block( ), VoxelShapes.block( ) } ;

    protected final VoxelShape[] shape_ls = new VoxelShape[ ] { VoxelShapes.empty( ), VoxelShapes.empty( ), VoxelShapes.empty( ), VoxelShapes.empty( ) } ;
    protected boolean isEmpty = true ;
    public RotateVoxelShape or( double sx, double sy, double sz, double ex, double ey, double ez ) {
        return this.join( sx, sy, sz, ex, ey, ez, IBooleanFunction.OR ) ;
    } ;
    public RotateVoxelShape join ( double sx, double sy, double sz, double ex, double ey, double ez, IBooleanFunction function ) {
        double startX = sx / 16d ;
        double startY = sy / 16d ;
        double startZ = sz / 16d ;
        double endX = ex / 16d ;
        double endY = ey / 16d ;
        double endZ = ez / 16d ;
        this.isEmpty = false ;
        for ( int i = 0 ; i < 4 ; i ++ ) {
            double[] rs = RotatePos.rotate( startX, startZ, .5f, RotatePos.RotateAngle.get( i ) ) ;
            double[] re = RotatePos.rotate( endX, endZ, .5f, RotatePos.RotateAngle.get( i ) ) ;
            shape_ls[i] = VoxelShapes.join( shape_ls[i], VoxelShapes.box( rs[0], startY, rs[1], re[0], endY, re[1] ), function ) ;
        } ;
        return this ;
    } ;
    public Build build( Direction baseDirection ) {
        return new Build( ( baseDirection.get2DDataValue( ) +2 ) & 3, this.isEmpty ? BOX.clone( ) : this.shape_ls.clone( ) ) ;
    } ;

}
