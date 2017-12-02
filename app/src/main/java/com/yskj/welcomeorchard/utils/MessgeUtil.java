package com.yskj.welcomeorchard.utils;

/**
 * Created by YSKJ-02 on 2016/12/24.
 */

public class MessgeUtil {
 public static String geterr_code(int code){
     String err_messge = null;
     switch (code){
         case 300301:
             err_messge = "内部错误";
         break;
         case 300302:
             err_messge = "参数异常";
             break;
         case 404:
             err_messge = "404 错误";
             break;
         case 100:
             err_messge = "密码错误";
             break;
         case 500:
             err_messge = "服务器内部错误";
             break;
         case 400:
             err_messge = "错误的请求";
             break;
         case 403:
             err_messge = "403 错误";
             break;
         case 401:
             err_messge = "401 错误";
             break;
         case 0:
             err_messge = "成功";
             break;
         case 700:
             err_messge = "数据库数据执行发生未知错误";
             break;
         case 763:
             err_messge = "GP值不足，不能完全获得收益，强行拆取将损失收益，是否强行拆取";
             break;
         case 762:
             err_messge = "红包不存在";
             break;
         case 753:
             err_messge = "GP值不足够，无法获取收益";
             break;
         case 759:
             err_messge = "本地生活不在消费时间内";
             break;
         case 701:
             err_messge = "可用资金不足";
             break;
         case 702:
             err_messge = "登录异常";
             break;
         case 704:
             err_messge = "实名认证失败";
             break;
         case 705:
             err_messge = "银行卡号已经存在";
             break;
         case 706:
             err_messge = "银行卡不存在";
             break;
         case 708:
             err_messge = "支付密码错误";
             break;
         case 709:
             err_messge = "身份证号码错误";
             break;
         case 710:
             err_messge = "生成订单信息错误";
             break;
         case 711:
             err_messge = "保存临时订单错误";
             break;
         case 712:
             err_messge = "微信支付获取临时订单错误";
             break;
         case 713:
             err_messge = "支付密码错误";
             break;
         case 714:
             err_messge = "手机号已经被注册";
             break;
         case 715:
             err_messge = "手机号不合法";
             break;
         case 716:
             err_messge = "未设置支付密码";
             break;
         case 717:
             err_messge = "理财投资金额转出失败";
             break;
         case 718:
             err_messge = "已领取";
             break;
         case 719:
             err_messge = "名字不符合要求";
             break;
         case 720:
             err_messge = "VB广告领取已经达到上限";
             break;
         case 721:
             err_messge = "用户的收货地址为空";
             break;
         case 722:
             err_messge = "短信每日次数限制";
             break;
         case 723:
             err_messge = "二维码无效";
             break;
         case 724:
             err_messge = "二维码已过期";
             break;
         case 725:
             err_messge = "会员不存在";
             break;
         /*case 726:
             err_messge = "不在粉丝列表中";
             break;*/
         case 727:
             err_messge = "商家不存在";
             break;
         case 728:
             err_messge = "商家未激活";
             break;
         case 729:
             err_messge = "不支持该支付类型";
             break;
         case 730:
             err_messge = "该用户已注册商店";
             break;
         case 731:
             err_messge = "红包已经拆过了";
             break;
         case 732:
             err_messge = "审批中";
             break;
         case 733:
             err_messge = "没有获奖信息";
             break;
         case 734:
             err_messge = "该用户未注册商店";
             break;
         case 735:
             err_messge = "本地生活服务不存在";
             break;
         case 736:
             err_messge = "本地生活服务购买限制";
             break;
         case 737:
             err_messge = "本地生活服务使用限制";
             break;
         case 738:
             err_messge = "本地生活订单不存在";
             break;
         case 739:
             err_messge = "本地生活订单状态错误";
             break;
         case 740:
             err_messge = "无权限发红包";
             break;
         case 741:
             err_messge = "没有拆广告红包的权限";
             break;
         case 742:
             err_messge = "广告红包无效范围";
             break;
         case 743:
             err_messge = "广告红包已拆完";
             break;
         case 744:
             err_messge = "当前用户已领取该广告红包";
             break;
         case 745:
             err_messge = "广告红包已过期";
             break;
         case 746:
             err_messge = "会员不允许发、接红包";
             break;
         case 747:
             err_messge = "广告红包已禁用";
             break;
         case 748:
             err_messge = "商家身份证已审核通过";
             break;
         case 800:
             err_messge = "保存订单信息错误";
             break;
         case 801:
             err_messge = "订单购买时金额不足";
             break;
         case 802:
             err_messge = "订单购买时库存不足";
             break;
         case 803:
             err_messge = "购买次数限制";
             break;
         case 804:
             err_messge = "活动未开始";
             break;
         case 805:
             err_messge = "活动已结束";
             break;
         case 806:
             err_messge = "无可拆红包";
             break;
         case 807:
             err_messge = "消费券不属于商家";
             break;
         case 810:
             err_messge = "验证码错误";
             break;
         case 101:
             err_messge = "未设置实名认证";
             break;
         case 102:
             err_messge = "身份证号已存在";
             break;
         case 751:
             err_messge = "GP值不足够，无法获取收益";
             break;
         case 754:
             err_messge = "商城订单不存在";
             break;
         case 755:
             err_messge = "商城已经完成，无需重新付款";
             break;
         case 756:
             err_messge = "订单异常，请联系客服";
             break;
         case 811:
             err_messge = "会员昵称已存在";
             break;
         case 902:
             err_messge = "不能转账给自己";
             break;
         case 901:
             err_messge = "超出单次提现额度";
             break;
         case 900:
             err_messge = "每日提现次数已用完";
             break;
         case 905:
             err_messge = "您有一笔提现正在处理中，不能再次提现";
             break;
         case 909:
             err_messge = "性别不符合要求";
             break;
         case 910:
             err_messge = "年龄不符合要求";
             break;
           case 911:
             err_messge = "您有一次预约正在审批中，不能再次预约";
             break;
         case 913:
             err_messge = "银行卡号格式有误";
             break;
         case 912:
             err_messge = "最多允许上传5张打款流水凭条图片";
             break;
         case 914:
             err_messge = "预约状态异常，请联系客服";
             break;
         case 103:
             err_messge = "分享人不存在";
             break;
         case 707:
             err_messge = "该手机号无权分享";
             break;
         case 921:
             err_messge = "一键拆红包权限已到期";
             break;
         case 926:
             err_messge = "一键拆红包功能已开通";
             break;
         case 922:
             err_messge = "未申请一键拆红包权限";
             break;
         case 925:
             err_messge = "一键拆红包功能已支付完成";
             break;
         case 934:
             err_messge = "身份证号已经被使用";
             break;
         case 1015:
             err_messge = "库存不足";
             break;
         case 1011:
             err_messge="包含库存不足的商品";
             break;
         case 1028:
             err_messge="不能购买自己分销的商品";
             break;
         case 1027:
             err_messge="低于供应商最低限额";
             break;
         case 1026:
             err_messge="用户分销商品不存在";
             break;
         case 1025:
             err_messge="收货地址不存在";
             break;
         case 1024:
             err_messge="商品规格不存在";
             break;
         case 1023:
             err_messge="供应商已存在";
             break;
         case 1022:
             err_messge="供应商不存在或审批未通过";
             break;
         case 1021:
             err_messge="商品不存在";
             break;
         case 1020:
             err_messge="商品不属于该商家的直属代理";
             break;
         case 1019:
             err_messge="超出了审批区域";
             break;
         case 1018:
             err_messge="代理申请地区必须包含自己商家地址";
             break;
         case 1017:
             err_messge="申请的代理区域不在同一地区";
             break;
         case 1016:
             err_messge="所申请代理区域太大";
             break;
         case 1014:
             err_messge="直供商品订单状态异常";
             break;
         case 1013:
             err_messge="直供商品订单已退款";
             break;
         case 1012:
             err_messge="直供商品订单不存在";
             break;
         case 1010:
             err_messge="订单状态错误";
             break;
         case 1009:
             err_messge="超出每次采购限额";
             break;
         case 1008:
             err_messge="商品类型错误";
             break;
         case 1007:
             err_messge="所选商品必须属于同一商家";
             break;
         case 1006:
             err_messge="商品不属于当前商家";
             break;
         case 1005:
             err_messge="商品不存在";
             break;
         case 1004:
             err_messge="区域代理状态不正常";
             break;
         case 1003:
             err_messge="区域代理信息不存在";
             break;
         case 1002:
             err_messge="条形码已存在";
             break;
         case 1001:
             err_messge="本地服务不存在";
             break;
         case 1000:
             err_messge="App版本过低，请更新App。";
             break;
         case 936:
             err_messge="申请区域必须为同级地区";
             break;
         case 935:
             err_messge="该地区已被申请代理";
             break;
         case 933:
             err_messge="已通过申请";
             break;
         case 932:
             err_messge="仪器申请参数异常";
             break;
         case 931:
             err_messge="未提交仪器申请";
             break;
         case 930:
             err_messge="仪器申请状态异常";
             break;
         case 928:
             err_messge="昵称过长，最多不能超过10位";
             break;
         case 927:
             err_messge="扫码支付临时记录不存在";
             break;
         case 920:
             err_messge="扫码支付已经完成";
             break;
         case 924:
             err_messge="已关闭一键拆红包权限";
             break;
         case 923:
             err_messge="一键拆红包临时记录已存在";
             break;
         case 919:
             err_messge="未查询到缴费单位";
             break;
         case 918:
             err_messge="订单异常";
             break;
         case 917:
             err_messge="订单已完成";
             break;
         case 916:
             err_messge="订单不存在";
             break;
         case 915:
             err_messge="您有一次审批不通过的预约，请修改后再次预约";
             break;
         case 908:
             err_messge="消费券已退款";
             break;
         case 907:
             err_messge="消费券已使用";
             break;
         case 906:
             err_messge="没有一键拆红包权限";
             break;
         case 904:
             err_messge="超出每日转账限额";
             break;
         case 903:
             err_messge="超出单次转账限额";
             break;
         case 819:
             err_messge="活动购买限制";
             break;
         case 818:
             err_messge="活动未开始";
             break;
         case 817:
             err_messge="活动已结束";
             break;
         case 816:
             err_messge="广告分类不存在";
             break;
         case 815:
             err_messge="云积分不足";
             break;
         case 814:
             err_messge="会员未开通过该VIP特权";
             break;
         case 813:
             err_messge="消费券不合法";
             break;
         case 812:
             err_messge="消费券不存在或已过期";
             break;
         case 820:
             err_messge="文件格式不正确";
             break;
         case 809:
             err_messge="文件格式不正确";
             break;
         case 808:
             err_messge="文件大小超过限制";
             break;


     }
     return err_messge;
 }
}
