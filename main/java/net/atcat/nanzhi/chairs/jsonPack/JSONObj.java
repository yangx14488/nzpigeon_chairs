package net.atcat.nanzhi.chairs.jsonPack;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** 这个类主要针对JSON做一些操作
 *  例如，从Map中读取出键值， 快速读取JSON到一个对象中（遵循Gson的规范）
 *
 * */
public abstract class JSONObj {

    public static Logger logger = LogManager.getLogger( ) ;

    public static class LogPrinter {
        public static void mismatchedClass ( Class<?> targetClass, Object inputObject ) {
            logger.error( "Object " +inputObject.getClass( ).getName( ) +"<" +inputObject +">" +" is not instance of " +targetClass.getName( ) ) ;
        } ;
        public static void mismatchedLength ( int targetLength, int actualLength ) {
            logger.error( "Mismatched length: Target length is " +targetLength +", But Actual length is " +actualLength ) ;
        } ;
        public static void jsonDeserializing ( String json, JsonSyntaxException exception ) {
            logger.error( "An error was encountered while deserializing the string: " +( json.length( ) > 64 ? json.substring( 0, 63 ) +"..." : json ), exception ) ;
        } ;
        public static void ioError( String path, IOException exception ) {
            logger.error( "An error was encountered while reading the path: " +path, exception ) ;
        } ;
        public static void jsonLoading ( String path ) {
           logger.info( "Loading json: " +path ) ;
        } ;

    } ;

    // 别用 / ，会获取到奇奇怪怪的东西
    private static final String ROOT_PATH = "../../../../../" ;
    // 获得根路径下的文件URL
    public static URL getURL( String path ) {
        return JSONObj.class.getResource( ROOT_PATH +path ) ;
    } ;
    // 将指定路径的文件读取为字符串
    public static String getString( String path, Charset charset ) throws IOException {
        int size ;
        URL url = getURL( path ) ;
        byte[] cache = new byte[2048] ; //2k
        StringBuilder ret = new StringBuilder();
        // 检查
        if ( url == null )
            throw new IOException( "Not Found File: " +path ) ;
        // 连接
        URLConnection connection = url.openConnection( ) ;
        // 传输编码
        connection.setRequestProperty( "Charset", charset.toString( ) ) ;
        // 连接
        connection.connect( ) ;
        // 取得输入流
        BufferedInputStream bin = new BufferedInputStream( connection.getInputStream( ) ) ;
        // 取得数据
        while ( ( size = bin.read( cache ) ) != -1 ) {
            ret.append( new String( cache, 0, size, charset ) ) ;
        } ;
        return ret.toString( ) ;
    } ;

    /* 从JSON文件读取到对象
    */
    @Nullable
    public static <T> T getObjectFromJSONFile( String path, Class<T> tClass ) {
        LogPrinter.jsonLoading( path ) ;
        Gson gson = new Gson( ) ;
        T ret = null ;
        try {
            String jsonStr = getString( path, StandardCharsets.UTF_8 ) ;
            try {
                ret = gson.fromJson( jsonStr, tClass ) ;
            } catch ( JsonSyntaxException e ) {
                LogPrinter.jsonDeserializing( jsonStr, e ) ;
            }
        } catch ( IOException e ) {
            LogPrinter.ioError( path, e ) ;
        }
        return ret ;
    }

}
