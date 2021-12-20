/* The MIT License (MIT)
Copyright © 2021. 南织鸽子<1448848683@qq.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package net.atcat.nanzhi.chairs.com;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** 方块注册类
 * 用法：
 * getRegistry = new Registry( <模组ID> )  // 创建注册器的实例
 * getRegistry.register( ... )  // 注册方块、物品等
 * getRegistry.register( getBus ) // 注册到总线
 *
 * 该文件虽然是基于MIT许可证开源的，不过你还是要保留一份作者信息。
 * @author    南织鸽子<1448848683@qq.com>
 * */

public class Registry {

    public static class Group {
        public final Map<String,Registry> registryMap = new HashMap<>( ) ;
        public Group add ( Registry registry ) {
            this.registryMap.put( registry.id, registry ) ;
            return this ;
        } ;
        public Group add ( Registry registry, Registry... rs ) {
            for ( Registry r : rs )
                this.add( r ) ;
            return this.add( registry ) ;
        } ;
        public <T extends IForgeRegistryEntry<T>> void forEach ( IForgeRegistry<T> forgeReg, Consumer<RegistryObject<T>> callback ) {
            this.registryMap.forEach( ( id, registry ) -> {
                DeferredRegister<?> data= registry.defMap.get( forgeReg ) ;
                if ( data != null ) {
                    DeferredRegister<T> defReg = (DeferredRegister<T>) data ;
                    defReg.getEntries( ).forEach( callback ) ;
                } ;
            } ) ;
        }
    } ;

    protected final String id ;
    protected final Registry father ;
    private boolean REG = false ;

    protected final Map<IForgeRegistry<?>,DeferredRegister<?>> defMap = new HashMap<>( ) ;

    public final DeferredRegister<Item> itemReg ;
    public final DeferredRegister<Block> blockReg ;
    public final DeferredRegister<Effect> effectReg ;
    public final DeferredRegister<Attribute> attributeReg ;
    public final DeferredRegister<SoundEvent> soundReg ;
    public final DeferredRegister<TileEntityType<?>> tileEntityReg ;
    public final DeferredRegister<EntityType<?>> entityReg ;

    public Registry(String modID ) {
        this( modID, null ) ;
    }
    public Registry(String modID, Registry father ) {
        this.id = modID ;
        this.father = father ;
        this.itemReg = createDef( ForgeRegistries.ITEMS ) ;
        this.blockReg = createDef( ForgeRegistries.BLOCKS ) ;
        this.effectReg = createDef( ForgeRegistries.POTIONS ) ;
        this.soundReg = createDef( ForgeRegistries.SOUND_EVENTS ) ;
        this.attributeReg = createDef( ForgeRegistries.ATTRIBUTES ) ;
        this.tileEntityReg = createDef( ForgeRegistries.TILE_ENTITIES ) ;
        this.entityReg = createDef( ForgeRegistries.ENTITIES ) ;
    }

    protected String getRegistryID( String name ) {
        return this.father == null ? name : father.getRegistryID( this.id +"/" +name ) ;
    }
    protected String namespace( ) {
        return this.father == null ? this.id : father.namespace( ) +"/" +this.id ;
    }

    // 用来创建或添加自身到一个新的群组里
    public Group group( ) {
        return new Group( ).add( this ) ;
    } ;
    public Registry group( Group group ) {
        if ( group != null )
            group.add( this ) ;
        return this ;
    } ;

    /** 用于创建注册器，注册器会添加到defArr里以供注册到总线
     * @param reg 注册类型
     * */
    private <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createDef( IForgeRegistry<T> reg ) {
        DeferredRegister<T> ret ;
        if ( this.father != null ) {
            ret = this.father.createDef( reg ) ;
        } else {
            DeferredRegister<?> def = defMap.get( reg ) ;
            if ( def != null ) {
                ret = (DeferredRegister<T>) def ;
            } else {
                ret = DeferredRegister.create( reg, this.id ) ;
                defMap.put( reg, ret ) ;
            } ;
        } ;
        return ret ;
    } ;

    /** 物品注册
     * @param name 物品ID
     * @param item 物品实例
     * @return 物品
     * */
    public Item register( String name, Item item ) {
        itemReg.register( getRegistryID( name ), ( ) -> item );
        return item ;
    }
    /** 方块注册
     * @param name 物品ID
     * @param block 方块实例
     * @param properties 物品属性
     * @return 方块
     * */
    public Block register( String name, Block block, Item.Properties properties ) {
        blockReg.register( getRegistryID( name ), ( ) -> block ) ;
        itemReg.register( getRegistryID( name ), ( ) -> new BlockItem( block, properties ) );
        return block ;
    }
    /** @param BlockItemIn 方块物品 */
    public Block register( String name, BlockItem BlockItemIn ) {
        blockReg.register( getRegistryID( name ), BlockItemIn::getBlock ) ;
        itemReg.register( getRegistryID( name ), ( ) -> BlockItemIn );
        return BlockItemIn.getBlock( ) ;
    }
    /** @param group 方块被分配的组 */
    public Block register( String name, Block block, ItemGroup group ) {
        blockReg.register( getRegistryID( name ), ( ) -> block ) ;
        itemReg.register( getRegistryID( name ), ( ) -> new BlockItem( block, new Item.Properties( ).tab( group ) ) );
        return block ;
    }
    /** 效果注册
     * @param name 物品ID
     * @param effectIn 效果
     * @return 效果
     * */
    public Effect register( String name, Effect effectIn ) {
        effectReg.register( getRegistryID( name ), ( ) -> effectIn ) ;
        return effectIn ;
    }
    /** 属性注册
     * @param name 物品ID
     * @param attr 属性
     * @return 属性
     * */
    public Attribute register( String name, Attribute attr ) {
        attributeReg.register( getRegistryID( name ), ( ) -> attr ) ;
        return attr ;
    }
    /** 声音注册
     * @param name 声音id
     * @param sound 声音
     * @return 属性
     * */
    public SoundEvent register( String name, SoundEvent sound ) {
        soundReg.register( getRegistryID( name ), ( ) -> sound ) ;
        return sound ;
    }
    public SoundEvent soundRegister( String name ) {
        return register( name, new SoundEvent( new ResourceLocation( this.namespace( ), name ) ) ) ;
    }
    /** 方块实体
     * @param name 物品ID
     * @param ts lambda表达式，要求返回一个方块实体的实例
     * @param blockIn 方块
     * @return 方块实体类型
     * */
    public <T extends TileEntity> TileEntityType<T> register ( String name, Supplier<T> ts, Block blockIn ) {
        TileEntityType<T> te = TileEntityType.Builder.of( ts, blockIn ).build( Util.fetchChoiceType( TypeReferences.BLOCK_ENTITY, getRegistryID( name ) ) ) ;
        tileEntityReg.register( getRegistryID( name ), ( ) -> te ) ;
        return te ;
    }

    /** 实体
     * @param name   实体ID
     * @param ts   lambda表达式，要求返回一个实体的实例
     * @param classification   实体类型
     * @param width   实体宽度
     * @param height   实体高度
     * @return 方块实体类型
     * */
    public <T extends Entity> EntityType<T> register ( String name, EntityType.IFactory<T> ts, EntityClassification classification, float width, float height ) {
        EntityType<T> entity = EntityType.Builder.of( ts, classification ).sized( width, height ).build( getRegistryID( name ) ) ;
        entityReg.register( getRegistryID( name ), ( ) -> entity ) ;
        return entity ;
    }
    public <T extends Entity> EntityType<T> register ( String name, EntityType.IFactory<T> ts, EntityClassification classification ) {
        return this.register( name, ts, classification, 2, 0.5f ) ;
    }
    public <T extends Entity> EntityType<T> register ( String name, EntityType.IFactory<T> ts, float width, float height ) {
        return this.register( name, ts, EntityClassification.MISC, width, height ) ;
    }
    public <T extends Entity> EntityType<T> register ( String name, EntityType.IFactory<T> ts ) {
        return this.register( name, ts, EntityClassification.MISC, 2, 0.5f ) ;
    }
    /** 注册
     * @param bus MC总线
     * */
    public void regBus( IEventBus bus, Consumer<Registry> callback ) {
        if ( this.father != null ) {
           this.father.regBus( bus, callback ) ;
        } else {
            if ( !REG ) {
                REG = true ;
                this.defMap.forEach( ( key, val ) -> {
                    val.register( bus ) ;
                } ) ;
                callback.accept( this ) ;
            }
        } ;
    }
    public void regBus( IEventBus bus ) {
        this.regBus( bus, reg -> { } ) ;
    }


}
