package net.atcat.nanzhi.chairs.jsonPack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class PackListObject extends JSONObj {

    private List<String> packlist ;

    public PackListObject( List<String> pl ) {
        this.packlist = pl ;
    } ;

    // 遍历
    public void forEach ( Consumer<String> callback ) {
        packlist.forEach( callback ) ;
    } ;

    // 取得列表
    public List<String> getList ( ) {
        return this.packlist;
    } ;

    // 从路径取得实例
    @Nullable
    public static PackListObject getObjectFromJSONFile(String path ) {
        return getObjectFromJSONFile( path, PackListObject.class ) ;
    } ;

}
