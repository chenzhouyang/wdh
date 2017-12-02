#version.2017
本次的需求具有颠覆性,改变了原来的业务模式.
#目的
原有业务涉及到敏感的资金交易,不利于公司长远发展.
本次改动主要以商品分销为核心,通过商家,广告增加客户粘性.
#需求描述
本次需求主要围绕绿尚客五级分销系统,同级紧缩补贴,积分系统和本地商家系统进行资源整合

## 五层分销
**名词解释**:
消费值: 用户购买商品所需要的资金
M值: 用户购买商品会赠送M值.
额度: 用户最多获取的资金额度.
**五层分销描述**: 

![Untitled Diagram (1).png](http://upload-images.jianshu.io/upload_images/3243080-a2ffdc450856bbf1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

用户等级分为 消费者, 推广员, 零售商,经销商,分销商,渠道商,董事.
**晋升规则**: 
注册用户为普通用户即消费者.
累计消费达到1000M值才能升级为推广员.
![Untitled Diagram (2).png](http://upload-images.jianshu.io/upload_images/3243080-c57ccd99fba6ce36.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
假设一个推广员推荐的有若干人,只有1个人消费累计了5万M值,则推广员完成了A部建设,推广员晋级为零售商,当推广员推广的人有两个,1个完成了5万M值积累,一个完成了10万M值积累,则用户完成了B部建设,晋升为经销商,依次类推,当推广者完成了A,B,C,D,E部建设就晋升为董事.

**tips:** M值积累为所有下方用户的绩效总和.即 A推荐了N个人,N个人+A 的M值总和 = 积累M值.

**等级分润**:
消费者推广的消费者,默认享有2%的分润比例.
依次类推,用户每个级别的分润相差 6%.

**程序逻辑**:
在程序逻辑中,我们需要这样理解:
何时才能触发晋升呢?
如果是推广员则晋升通过部门的建立,部门的建立通过累计M值,M值通过购物时候获取,那么我们需要在购物后对推广员进行更新级别操作.
```java
//购物赠送M值
double mcoin = 100.00;
double totalMcoin = mcoin + user.getTotalMCoin();
//判断用户当前等级,如果是消费者
if(user.getTotalCoin>=1000){
//进行自身晋级处理
}
...
//判断是否触发上层推广员晋级,门槛,5w,10w,15w,25w,30w,
//如果满足触发条件则更新上层推广者晋级记录,比如我们使用ABCDE来对应相应的门槛.
//mongodb中可以记录类似key,value的样式:level_record: {"user_333": 'A',"user_444": 'B',"user_533": 'A'}
//最终我们可以获取上层推广者level_record来判断更新上层推广者等级.
//比如,进行去重[ABCDE] 或者BCD,ABC,BCDE,DE,我们通过判断去重后的数量就可以了.
```
分润是如何实现的呢?

![Untitled Diagram (3).png](http://upload-images.jianshu.io/upload_images/3243080-dd17d77d594006ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
用户购买商品,商品将赠送一定的M值,这部分M值将用于资金的分配,消费者购买商品,如果存在消费者推荐该消费者则获利2%,逐级往上找,假如中间隔层,则上层分配缺层的资金,比如,推广会员分配6%,上层没有零售商,则上层经销商分配12%.
##紧缩机制
该机制主要鼓励同级别的用户.紧缩机制触发的原因是当对应级别收取到了级差,如何理解呢? 五级分销中不同级别的人讲分配不同的利润,根据级别不同,获取利润之后触发紧缩机制.

![Untitled Diagram (4).png](http://upload-images.jianshu.io/upload_images/3243080-29b4514ed5ad20a6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图中,出现两个零售商,因为五层分润开启后,推广员上面的零售商获取6%,零售商触发紧缩机制,向上查找同等级为零售商的5个用户,分配资金.

**tips:** 紧缩机制并不是必须是紧邻的同级别,而是向上查询离自己最近的5个同级别.没有则沉淀. 


