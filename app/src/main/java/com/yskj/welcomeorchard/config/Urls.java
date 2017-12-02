package com.yskj.welcomeorchard.config;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class Urls {

    public static final String HEAD = Ips.API_URL + "api/article_categories/610001/articles/";//首页notice滚动条

    public static final String ADDRESS = Ips.PHPURL + "/App/Port/userAddress/sk/" + Ips.SK + "/act/4/uid/";//获取收货地址列表  后拼接userid

    public static final String MORENADDRESS = Ips.PHPURL + "/App/Port/userAddress/sk/" + Ips.SK + "/act/5/uid/";//更新默认地址 后拼接userid +/address_id/再拼接addressid

    public static final String DELETEADDRESS = Ips.PHPURL + "/App/Port/userAddress/sk/" + Ips.SK + "/act/2/uid/";//删除地址列表 后拼接userid +/address_id/再拼接addressid
    //更新收货地址
    public static final String GENXINADDRESS = Ips.PHPURL + "/App/Port/userAddress/sk/" + Ips.SK + "/act/3/uid/";

    //增加收货地址
    public static final String ADDADDRESS = Ips.PHPURL + "/App/Port/userAddress/sk/" + Ips.SK + "/act/1/uid/";
    //首页商品分类列表
    public static final String GOODSCATEGORYLIST = Ips.PHPURL + "/App/GoodsPort/goodsCategoryList/sk/" + Ips.SK;
    //首页商品列表
    public static final String GOODSLIST = Ips.PHPURL + "/App/GoodsPort/goodsList/sk/" + Ips.SK ;
    //分类搜索商品接口
    public static final String SEARCHGOODSLIST = Ips.PHPURL + "/App/GoodsPort/goodsList/sk/" + Ips.SK + "/";
    //跳转到商品详情url
    public static final String GOODSDETAIL = Ips.PHPURL + "/App/Goods/goodsInfo/id/";
    //调支付
    public static final String PAY = Ips.API_URL + "api/v1/user/fund/recharge/pay_info";
    //订单列表
    public static final String ORDERLIST = Ips.PHPURL + "/App/OrderPort/orderList";
    //确认订单
    public static final String ORDERTRUE = Ips.PHPURL + "/App/OrderPort/orderConfirm";
    //取消订单
    public static final String ABOLISH = Ips.PHPURL + "/App/OrderPort/cancelOrder/sk/" + Ips.SK;
    //历史弹窗
    public static final String WINDOWS = Ips.PHPURL + "/App/NewsPort/popupNewsList/sk/" + Ips.SK;
    //首次获取弹窗
    public static final String FIRSTWINDOWS = Ips.PHPURL + "/App/NewsPort/indexNews/sk/" + Ips.SK;
    //消息盒子列表
    public static final String NEWSBOX = Ips.API_URL + "api/v1/user/msg/message/records";
    //登陆
    public static final String LOGIN = Ips.API_URL + "api/v1/auth/oauth/token";
    //加入删除购物车   1加入购物车 2 删除
    public static final String ADDTOSHOPPINGCART = Ips.PHPURL + "/App/CartPort/cartAddDel/sk/" + Ips.SK + "/ope_type/";
    //大额商品提交订单接口
    public static final String WHOLESALE = Ips.PHPURL + "/App/OrderPort/createOrder/sk/"+ Ips.SK;
    //查询购物车列表及修改购物车数量，选中状态
    public static final String QUERYSHOPPINGCART = Ips.PHPURL + "/App/CartPort/cartList/sk/" + Ips.SK + "/uid/";
    //    public static final String QUERYSHOPPINGCART = "http://192.168.0.186/App/CartPort/cartList/sk/"+Ips.SK+"/uid/";
    //确认订单接口
    public static final String CONFIRMORDER = Ips.PHPURL + "/App/CartPort/confirmOrder/sk/" + Ips.SK + "/token/";
    //提交订单接口
    public static final String SUBMITORDER = Ips.PHPURL + "/App/OrderPort/generateOrder/sk/" + Ips.SK + "/token/";

    //转账
    public static final String ACCOUNTS = Ips.API_URL + "api/v1/user/fund/transfer/apply";

    //转账记录
    public static final String ACCOUNTLIST = Ips.API_URL + "api/v1/user/fund/transfer/records";

    //提现记录
    public static final String RECORDLIST = Ips.API_URL + "api/v1/user/fund/withdraw/records";

    //提现
    public static final String REEORD = Ips.API_URL + "api/v1/user/fund/withdraw/apply";

    //账单
    public static final String REVEBU = Ips.API_URL + "api/v1/user/msg/profitLog/records";

    //请求验证码
    public static final String VIERFI = Ips.API_URL + "api/v1/sms/sendCode";

    //验证验证码
    public static final String VIERFICODE = Ips.API_URL + "api/v1/sms/checkCode";

    //查询银行卡
    public static final String BANKLIST = Ips.API_URL + "api/v1/user/card/findByUserID";

    //删除银行卡
    public static final String DELBANK = Ips.API_URL + "api/v1/user/card/deleteById";

    //添加银行卡
    public static final String ADDBANK = Ips.API_URL + "api/v1/user/card/save";

    //实名认证
    public static final String IDENRITY = Ips.API_URL + "api/v1/user/realname/save";

    //查询实名认证
    public static final String REALNAME = Ips.API_URL + "api/v1/user/realname/show";

    //查询分享记录
    public static final String FINDPAGINGLIST = Ips.API_URL + "api/v1/user/fans/findPagingList";

    //更新银行卡信息
    public static final String UPDATEBANK = Ips.API_URL + "api/v1/user/card/updateById";

    //测试轮播图
    public static final String MAINCB = Ips.PHPURL + "/app/Article/adList/sk/2ab99a6af22365686e97992df974d5c2/pid/404";

    //注册
    public static final String REGISTER = Ips.API_URL + "api/v1/user/save";

    //未登录修改会员密码
    public static final String NOTLOGINPW = Ips.API_URL + "api/v1/user/passwordUpdateNotLogin";

    //登录后修改会员密码
    public static final String UPDATEPASSWORD = Ips.API_URL + "api/v1/user/updatePassword";

    //修改支付密码
    public static final String UPDATEACCOUNT = Ips.API_URL + "api/v1/user/updateAccountPassword";

    //获取个人信息
    public static final String USERINFO = Ips.API_URL + "api/v1/user/getInfo";

    //获取已拆红包列表
    public static final String STAYREDLIST = Ips.API_URL + "api/v1/red/list";
    //获取待拆红包列表
    public static final String REDSMAL = Ips.API_URL + "api/v1/red/list/small";
    //拆红包
    public static final String CRED = Ips.API_URL + "api/v1/red/open";
    //红包详情
    public static final String REDCRED = Ips.API_URL + "api/v1/red/consumeRed/detail";
    //买家秀列表
    public static final String BUYSHOWLIST = Ips.PHPURL + "/App/BuyershowPort/commentList/sk/" + Ips.SK;
    //买家秀上传单张图片到php
    public static final String BUYSHOWPOSTSIMPLEIMG = Ips.PHPURL + "/App/BuyershowPort/uploadImg/sk/" + Ips.SK;
    //买家秀上传内容及图片到php
    public static final String BUYSHOWPOSTALL = Ips.PHPURL + "/App/BuyershowPort/addComment/sk/" + Ips.SK;

    //上传头像
    public static final String UPDATRSVTEA = Ips.API_URL + "api/v1/mgs/file/imageUpload";

    //请求更新头像
    public static final String UPDATEPORFI = Ips.API_URL + "api/v1/user/updatePortrait";

    //充值完成后完成订单交易
    public static final String PAYORDER = Ips.API_URL + "api/v1/order/pay";

    //本地生活购买接口
    public static final String CREATE = Ips.API_URL + "api/v1/life/goodOrder/pay";
    //供应商购买接口
    public static final String MGORDERPAY = Ips.API_URL + "api/v1/life/mgorder/pay";
    //分享接口
    public static final String UMSTR = Ips.PHPURL + "/index.php?m=Mobile&c=Goods&a=goodsInfo&id=";

    //订单支付php
    public static final String PHPORDER = Ips.PHPURL + "/App/OrderPort/payOrder/sk/" + Ips.SK;

    //修改会员昵称
    public static final String UPDATENICKNAME = Ips.API_URL + "api/v1/user/updateUserInfo";

    public static final String REDRECORD = Ips.API_URL + "v1/api/adRedStatistic/querySendAdReds";
    public static final String OBTAINRED = Ips.API_URL + "v1/api/adRedStatistic/queryReceiveAdReds";

    //广告红包详情
    public static final String adRedDetailUrl = Ips.API_URL + "v1/api/adRed/detail";

    //拆广告红包
    public static final String ADVEROPENRED = Ips.API_URL + "v1/api/adRed/open";

    //点赞
    public static final String thumbsUp = Ips.API_URL + "v1/api/thumbUp";

    //广告红包手续费
    public static final String CHARGE = Ips.API_URL + "v1/api/systemProperty/getSysPropeties";

    //本地生活订单查询接口
    public static final String LOCALSERVERORDER = Ips.API_URL + "api/v1/life/goodOrder/orderList";

    //本地生活订单退款接口
    public static final String LOCALSERVERREFUND = Ips.API_URL + "api/v1/life/goodOrder/coupon/refund";

    //本地生活查询接口
    public static final String LOCALSERVERFIND = Ips.API_URL + "api/v1/life/list";
    //本地生活分类弹窗
    public static final String LOCALSERVERPOPWIN = Ips.API_URL + "api/v1/life/category/list";
    //本地生活团购详情
    public static final String LOCALSERVERDETAIL = Ips.API_URL + "api/v1/life/detail?lifeId=";
    //城市列表
    public static final String BUSINESSCITY = Ips.API_URL + "api/v1/life/city/list";
    //本地生活搜索
    public static final String SOUSUO = Ips.API_URL + "api/v1/life/list";

    //商家入住
    public static final String APPLY = Ips.API_URL + "api/v1/life/shop/apply";

    //获取商家信息
    public static final String BUSINESS = Ips.API_URL + "api/v1/life/shop/info";


    //查询实名姓名
    public static final String GETREALNAME = Ips.API_URL + "api/v1/user/realname/getRealName";

    //查询手机号是否存在
    public static final String FINDBYMOBILE = Ips.API_URL + "api/v1/user/findByMobile";

    public static final String APPINTRO = Ips.PHPURL + "/App/NewsPort/adList/type/0/sk/" + Ips.SK;

    //查询分红宝信息
    public static final String REDPRIZE = Ips.API_URL + "api/v1/red/redPrize/info";
    //查询会员特权（一键拆红包）状态
    public static final String USERVIP = Ips.API_URL + "api/v1/user/vip/isUserVIPExist";

    //开通vip服务
    public static final String OPENVIP = Ips.API_URL + "api/v1/user/vip/payUserVip";
    //更新vip服务
    public static final String UPDATE = Ips.API_URL +"api/v1/user/vip/changeStatus";
    //关闭自动续费
    public static final String CLOSEVIP = Ips.API_URL + "api/v1/user/vip/close";

    public static final String QRCODEFK = Ips.API_URL + "api/v1/life/payCode/get";//获取二维码标识

    //扫码付款
    public static final String CODEPAYMENT = Ips.API_URL + "api/v1/life/payCode/scanCodeFinish";

    //查询物流信息
    public static final String LOGISTICS = "http://m.kuaidi100.com/query?";

    //草稿  发布后的红包列表  state(0是草稿 1是已发布) uid是用户id
    public static final String ADDRAFT = Ips.PHPURL + "/App/AdPort/ad_redList/sk/"+Ips.SK;

    //广告模板标签接口
    public static final String ADCHOOSEVERSION =Ips.API_URL +"api/v1/red/adRed/category/list";

    //广告红包模版列表
    public static final String ADTEMPLATELIUST =Ips.PHPURL +"/App/AdPort/ad_redtemplateList/sk/"+Ips.SK+"/cid/";

    //广告红包样例列表
    public static final String ADSIMPLELIST =Ips.PHPURL +"/App/AdPort/ad_redExampleList/sk/"+Ips.SK+"/cid/";

    //使用样例  使用样例实质就是复制该样例成为草稿，并对草稿进行编辑
    public static final String ADVERSIONWEB =Ips.PHPURL +"/App/Ad/ad_red_exampleUse";
    //草稿箱中点击编辑广告接口
    public static final String ADDRAFTEDIT =Ips.PHPURL +"/App/Ad/ad_red_pagesEdit";

    //广告清除用户对应的临时广告红包数据
    public static final String ADREDPAGERCLEAR = Ips.PHPURL +"/App/AdPort/ad_red_pagesClear";
    //新增或者修改广告红包，开始选择模板，新增广告页面
    public static final String ADEDITREDPAGES = Ips.PHPURL +"/App/Ad/ad_red_pages";

    //拆广告红包
    public static final String ADOPENRED = Ips.API_URL +"api/v1/red/adRed/open";
    //获取广告红包详情
    public static final String ADREDDETAIL = Ips.API_URL +"api/v1/red/adRed/detail";
    //发广告红包
    public static final String ADSENDRED = Ips.API_URL +"api/v1/red/adRed/completeAdRedPay";
    //统计收到的广告红包
    public static final String RECEIVEADRED = Ips.API_URL +"api/v1/red/adRed/receiveAdRed";
    //统计发送的广告红包
    public static final String SENDADRED = Ips.API_URL +"api/v1/red/adRed/sendAdRed";
    //广告红包轮播
    public static final String CAROUSE = Ips.PHPURL+"/App/NewsPort/adList/sk/"+Ips.SK+"/type/";

    //广告红包删除我的草稿接口
    public static final String DELDRAFT = Ips.PHPURL +"/App/AdPort/ad_redDel/sk/"+Ips.SK;
    //预约
    public static final String BESWPEAK = Ips.API_URL+"api/v1/order/largeGoodOrder/bespeak";
    //预约列表
    public static final String INTEROLIST = Ips.API_URL+"api/v1/order/largeGoodOrder/list";
    //预约详情
    public static final String DETAIL = Ips.API_URL+"api/v1/order/largeGoodOrder/detail";
    //获取公司账号
    public static final String INFOR = Ips.API_URL+"api/v1/order/companyAccount/info";
    //获取首页广告
    public static final String ARTICLE = Ips.PHPURL+"/App/NewsPort/articleList/type/0/sk/"+Ips.SK;
    //更新app
    public static final String UPDATEAPP = "https://static.wdh158.com/apk_upload.json";
    //下载app
    public static final String DOWNLOADAPP = Ips.PHPURL+"/App/NewsPort/app_download/sk/2ab99a6af22365686e97992df974d5c2?type=apk&file=";
    //首页当下的我们接口
    public static final String ALLFRAGMENTHEMYSELF = Ips.PHPURL+"/App/NewsPort/articleList/type/0/sk/"+Ips.SK;
    //首页商品促销
    public static final String ALLFRAGMENTHELISTTIME = Ips.PHPURL+"/App/Port/indexPromotionList/sk/"+Ips.SK;
    //首页推荐产品
    public static final String ALLFRAGMENTRECOMMEND = Ips.PHPURL+"/App/GoodsPort/goodsList/is_recommend/1/sk/"+Ips.SK;
    //商品详情
    public static final String COMMODITY = Ips.PHPURL+"/App/GoodsPort/goodsInfo/sk/"+Ips.SK+"/id/";
    //唯多惠新人专属
    public static final String WDGFRAGMAENTNEWPEOPLE = Ips.PHPURL+"/App/NewsPort/adList/type/5/sk/"+Ips.SK;
    //唯多惠出品fragment 分类接口
    public static final String WDGFRAGMAENTGOODCATEGORY = Ips.PHPURL+"/App/GoodsPort/goodsCategoryList/sk/"+Ips.SK+"/id/";
    //图文详情
    public static final String GOODSDETA = Ips.PHPURL+"/App/Goods/goodsDetail/id/";
    //刷新token值
    public static final String REFRE = Ips.API_URL+"api/v1/auth/oauth/token";
    //家纺底部bottom接口
    public static final String HOMETEXTILEGRIDBOTTOM = Ips.PHPURL+"/App/NewsPort/adList/type/6/sk/"+Ips.SK;
    //悬浮框设置按钮
    public static final String SUSPENT = Ips.PHPURL+"/App/NewsPort/adList/type/7/sk/"+Ips.SK;
    //清除失效商品
    public static final String CLEARAWAY = Ips.PHPURL+"/App/CartPort/clearCartFailgoods/sk/"+Ips.SK+"/uid/";
    //一键拆红包创建临时订单
    public static final String SETUP = Ips.API_URL+"api/v1/user/vip/create";
    //扫码支付创建临时记录
    public static final String SCANCODETMP = Ips.API_URL+"api/v1/life/payCode/scanCodeTmp";
    //发送广告红包创建临时记录
    public static final String CREATEADRED = Ips.API_URL+"api/v1/red/adRed/createAdRed";
    //本地生活创建临时订单
    public static final String LOCALINGTEMPORARY = Ips.API_URL+"api/v1/life/goodOrder/billing";

    public static final String ONEBUYGOT = Ips.API_URL+"api/v1/order/activity/reward/list";
    //七牛云接口
    public static final String QNYJSON = "http://oojnq6nur.bkt.clouddn.com/wdh_sign.json";
    //再次支付
    public static final String AGAINPAY = Ips.PHPURL+"/App/OrderPort/payOrderAgain/sk/"+Ips.SK+"/token/";
    //积分大转盘奖项列表接口
    public static final String PRIZELIST = Ips.PHPURL+"/App/BigwheelPort/big_wheelList/sk/"+Ips.SK;
    //算出选中商品，获取中奖信息接口
    public static final String PRIZERESULT = Ips.PHPURL+"/App/BigwheelPort/big_wheel_selectedAward/sk/"+Ips.SK+"/token/";
    //中奖名单接口
    public static final String PRIZEGOT = Ips.PHPURL+"/App/BigwheelPort/big_wheel_awardList/sk/"+Ips.SK;
    //中奖商品,提交领取礼品的地址信息接口
    public static final String PRIZECOMMITADDRESS = Ips.PHPURL+"/App/BigwheelPort/big_wheel_receiveAward/sk/"+Ips.SK;
    //查询仪器套餐列表
    public static final String DECTION = Ips.API_URL+"api/v1/life/local/getInfo";
    //扫码检测创建临时订单
    public static final String SCANCODETMPBILL = Ips.API_URL+"api/v1/life/payCode/IESCPCreateBill";
    //订单支付
    public static final String GOODORDERPAY = Ips.API_URL+"api/v1/life/goodOrder/pay";
    //请求资金
    public static final String MONEYINFO  = Ips.API_URL+"api/v1/user/accountInfo";
    //大宗商品回调
    public static final String IESCPF  = Ips.API_URL+"api/v1/life/payCode/IESCPFinish";
    //新版本地生活列表
    public static final String NEWLOCALGOODSLIST = Ips.API_URL + "api/v1/life/shop/list";
    //查询热搜关键字
    public static final String KEYWOEDLIST  = Ips.API_URL+"api/v1/life/goods/keywordList";
    //分页查询微商货源商品列表
    public static final String KEYWOEDGOODSLIST  = Ips.API_URL+"api/v1/life/goods/list";
    //分页查询某商铺的商品列表
    public static final String SHOPID  = Ips.API_URL+"api/v1/life/goodsListByShopId";
    //查询商家信息
    public static final String SHOPDETAIL  = Ips.API_URL+"api/v1/life/shop/detail";
    //获取订单列表
    public static final String MGOESWE  = Ips.API_URL+"api/v1/life/mgorder/list";
    //获取奖励金
    public static final String BOUNTS  = Ips.API_URL+"api/v1/life/mgorder/getBonus";
    //供应商品详情
    public static final String BOUNTSDETIAL  = Ips.API_URL+"api/v1/life/goods/detail";
    //创建直销商品订单
    public static final String MGORDER  = Ips.API_URL+"api/v1/life/mgorder/create";
    //取消供应商订单
    public static final String CANCEL  = Ips.API_URL+"api/v1/life/mgorder/cancel";
    //供应商订单确认收货
    public static final String CONFIRMGOODS  = Ips.API_URL+"api/v1/life/mgorder/confirmGoods";
    //领取奖励金
    public static final String GETBONUS  = Ips.API_URL+"api/v1/life/mgorder/getBonus";
    //根据millid获取商家信息
    public static final String MILLBYID  = Ips.API_URL+"api/v1/life/mill/getById";
    //获取邮费
    public static final String POSTAGE  = Ips.API_URL+"api/v1/life/mgorder/getPostage";
    //获取分销商商品
    public static final String DISTRITION  = Ips.API_URL+"api/v1/life/mgorder/dist/goodsList";
    //创建分销商的订单
    public static final String DISTRI  = Ips.API_URL+"api/v1/life/mgorder/dist/create";
    //修改支付密码
    public static final String NEWPAYPASSWORD = Ips.API_URL + "/user/updateAmPass";
    //修改登陆密码
    public static final String NEWLOGINPASSWORD = Ips.API_URL + "/user/updatePass";

    //获取验证码
    public static final String GOTCODE = Ips.API_URL + "/sms/code/send";

}
