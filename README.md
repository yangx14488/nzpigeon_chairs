## 你好，这里是椅子模组的开源代码。
源码位于 main 目录下。

## API
源码位于 api 目录下。

#### 使用方法
将两个类放到你的包里，随后访问 NZPigeonSit.registry( DeferredRegister<EntityType<?>> defReg ) 函数。  
这个函数是用来注册生物实体的，需要传入你的注册器（生物），不访问这个方法可能会导致若干个错误。  
这个函数需要在将注册器注册到总线（IEventBus）之前访问。  
  
#### 椅子方块类
NZPigeonSit.ChairBlock  
  
你可以继承这个类来构建你自己的椅子方块，目前没考虑通过接口实现。  
  
#### API
NZPigeonSit.api.isLivingEntitySittingOnPos：检查一个坐标点周围是否有生物坐着  
  
NZPigeonSit.api.sitToChair：让一个生物坐在指定的坐标点  
NZPigeonSit.api.isValidityLocation：坐下的合理性检查  
