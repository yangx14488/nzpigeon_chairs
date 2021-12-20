package net.atcat.nanzhi.chairs;

import net.atcat.nanzhi.chairs.com.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// 应该设置允许定义可以坐下的分类，或者方块
@Mod( NZChairs.ID )
@Mod.EventBusSubscriber( modid = NZChairs.ID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class NZChairs {

    public static final String ID = "nzpigeon_chairs" ;
    public static final String ACRONYM_ID = "nzchairs" ; // 包用的
    public static final Registry REG = new Registry( ID ) ; // 注册器
    public static final Registry.Group REG_GROUP = REG.group( ) ; // 构建一个默认添加注册器的群组

    public NZChairs( ) {

        IEventBus bus = FMLJavaModLoadingContext.get( ).getModEventBus( ) ;

        // 加载
        NZChairsEntity.load( ) ;
        NZChairsBlocks.load( ) ;
        NZChairsEffect.load( ) ;

        // 加载包
        JSONDataPack.regPacks( bus, REG_GROUP ) ;

        // 将所有的注册器注册进总线
        REG_GROUP.registryMap.forEach( ( nameSpace, registry ) -> registry.regBus( bus ) ) ;

    } ;


} ;
