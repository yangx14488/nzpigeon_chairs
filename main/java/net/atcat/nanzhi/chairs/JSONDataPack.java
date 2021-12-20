package net.atcat.nanzhi.chairs;

import net.atcat.nanzhi.chairs.com.Registry;
import net.atcat.nanzhi.chairs.com.block.AssCancerChair;
import net.atcat.nanzhi.chairs.com.block.PackChair;
import net.atcat.nanzhi.chairs.jsonPack.ChairBlocksObj;
import net.atcat.nanzhi.chairs.jsonPack.PackListObject;
import net.atcat.nanzhi.chairs.jsonPack.PackObj;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.Map;

public class JSONDataPack {

    private static final PackListObject packStrList = PackListObject.getObjectFromJSONFile( "chair_packs.json" ) ;
    public static final ChairBlocksObj chairBlockList = ChairBlocksObj.getObjectFromJSONFile( "chair_blocks.json" ) ;
    public static final Map<String,PackObj> packList = new HashMap<>( ) ;

    public static String getPackID ( String id ) {
       return NZChairs.ACRONYM_ID +"_pack_" +id ;
    } ;

    static {
        if ( JSONDataPack.packStrList != null ) {
            JSONDataPack.packStrList.forEach( id -> {
                String packID = getPackID( id ) ;
                PackObj pack = PackObj.getObjectFromJSONFile( "assets/" +packID +"/pack.json" ) ;
                if ( pack != null ) {
                    packList.put( packID, pack ) ;
                } ;
            } ) ;
        } ;
    } ;

    public static void regPacks( IEventBus bus, Registry.Group group ) {
        JSONDataPack.packList.forEach( ( packID, pack ) -> {
            // 创建新的注册器并添加到群组里
            Registry reg = new Registry( packID ).group( group ) ;
            // 遍历并注册方块
            pack.blocks.forEach( entry -> reg.register( entry.id, entry.assCancer ? new AssCancerChair( entry ) : new PackChair( entry ), ItemGroup.TAB_DECORATIONS ) ) ;
        } ) ;
    } ;


}
