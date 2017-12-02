package com.yskj.welcomeorchard.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 创建日期 2017/6/12on 10:33.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ShowErrorCode {
    public static void getShowdiag(Context context,String mess){
        new AlertDialog.Builder(context).setTitle("小果提示")//设置对话框标题
                .setMessage(mess)//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                    }
                }).show();//在按键响应事件中显示此对话框
    }
    public static String getErrorMessage(int code){
        String message = null;
        switch (code){
            case 0:
                message = "成功";
                break;
            case 600:
                message = "验证码不正确";
                break;
            case 650:
                message = "您的操作过于频繁，请稍后再试";
                break;
            case 660:
                message = "今日短信发送次数已用完";
                break;
            case 661:
                message = "验证码失败次数过多，请重新获取验证码";
                break;
            case 300301:
                message = "服务器忙请稍后再试";
                break;
            case 300302:
                message = "参数异常";
                break;
            case 700:
                message = "登陆异常";
                break;
            case 701:
                message = "手机号已注册";
                break;
            case 702:
                message = "手机格式不正确";
                break;
            case 703:
                message = "密码不能有特殊符号";
                break;
            case 704:
                message = "推荐人不存在";
                break;
            case 705:
                message = "主人，你还没有空地哦！";
                break;
            case 706:
                message = "新密码为空或新密码格式不正确";
                break;
            case 707:
                message = "银行卡号不正确";
                break;
            case 708:
                message = "请选择支行";
                break;
            case 709:
                message = "请填写持卡人姓名";
                break;
            case 710:
                message = "银行卡不存在";
                break;
            case 711:
                message = "传输银行卡信息有误";
                break;
            case 712:
                message = "用户信息获取失败";
                break;
            case 713:
                message = "您只能添加一张银行卡";
                break;
            case 714:
                message = "用户信息获取失败";
                break;
            case 715:
                message = "推荐人手机格式不正确";
                break;
            case 716:
                message = "填写信息不全";
                break;
            case 717:
                message = "超出购买限制，种子成熟一次就可多开辟一块土地";
                break;
            case 718:
                message = "积分不足，请核对积分与订单";
                break;
            case 719:
                message = "用户不存在";
                break;
            case 800:
                message = "生成订单信息错误";
                break;
            case 801:
                message = "保存临时订单错误";
                break;
            case 802:
                message = "没有设置支付密码";
                break;
            case 803:
                message = "支付密码错误";
                break;
            case 804:
                message = "用户名或密码错误";
                break;
            case 805:
                message = "没有开启的市场";
                break;
            case 806:
                message = "种子已售空";
                break;
            case 807:
                message = "用户或市场不存在";
                break;
            case 808:
                message = "市场已关闭或未开启";
                break;
            case 809:
                message = "没种子别瞎挂单";
                break;
            case 810:
                message = "请勿重复点击查收";
                break;
            case 811:
                message = "收益有错误";
                break;
            case 812:
                message = "主人，这地已种过了，你种地的速度要与分享农户的速度同步哦！";
                break;
            case 813:
                message = "兄弟别乱拿别人种子，小心手";
                break;
            case 814:
                message = "主人，你也太小气了，种子不够一块地需要10斤哦！";
                break;
            case 815:
                message = "主人，你也太急了，没成熟就采摘果果受不了的！";
                break;
            case 816:
                message = "您还没有添加银行卡，提现不能完成";
                break;
            case 817:
                message = "提现成功请耐心等待1-2个工作日！";
                break;
            case 818:
                message = "老密码错误,你是不是这号的主人哦！别瞎搞！";
                break;
            case 819:
                message = "主人，你知道貔貅吗，只进不出会影响你人生的格局哦！";
                break;
            case 820:
                message = "主人，刚开辟的地不需要施肥，腾出点时间去分享吧！";
                break;
            case 821:
                message = "你这种子自己摘下来，没有经过种子科研所处理,赶紧挂单去吧";
                break;
            case 822:
                message = "主人，花钱买的种子不去种植，是不是开拓市场累晕了？";
                break;
            case 823:
                message = "主人，点击化肥按钮果果立刻帮你施肥，你不会累的！";
                break;
            case 824:
                message = "施肥成功，恭喜您成功解锁一块土地，您有可以购买新土地了！";
                break;
            case  825:
                message = "升级订单才有可能抢到种子！";
                break;
            case  826:
                message = "您有预定订单未处理，试试升级订单";
                break;
            case  827:
                message = "真遗憾, 一会再来抢属于你的种子吧";
                break;
            case  828:
                message = "抢购成功！种子正在飞，请耐心等待";
                break;
            case 829:
                message = "没有粉丝";
                break;
            case 830:
                message = "主人，你太棒了，有名犬看守再也不怕贼惦记了，腾出时间去分享人吧！";
                break;
            case 831:
                message = "主人，好狗喜欢有能力的人，提升你的分享速度，获得名犬不是梦！";
                break;
            case 832:
                message = "没有耕种的地了";
                break;
            case 833:
                message = "对方账户不存在，请核对对方手机号（账户）";
                break;
            case 834:
                message = "主人，你有种子还没有交易，有钱也不能任性哦！";
                break;
            case 835:
                message = "种子收益订单不存在";
                break;
            case 836:
                message = "种子订单已收米";
                break;
            case 837:
                message = "种子订单未成交，请耐心等待";
                break;
            case 838:
                message = "您有种子订单未处理，请耐心等待";
                break;
            case 839:
                message = "支付宝调用积分有误，请及时反馈意见";
                break;
            case 840:
                message = "充值失败";
                break;
            case 841:
                message = "真遗憾，你偷了一块空地，快去试试别家吧";
                break;
            case 842:
                message = "一不小心跑到了自家地里，去别家看看吧";
                break;
            case 843:
                message = "这块地被偷太多啦，留他余种，给他发展机会，下次再来吧！";
                break;
            case 844:
                message = "今天偷得太多啦，告诉你个小秘密，种地越多偷的越广哦";
                break;
            case 845:
                message = "你已经偷过这块地啦，换一块吧";
                break;
            case 846:
                message = "你的仓库是不是漏啦，先等小果修修吧";
                break;
            case 847:
                message = "系统未初始化种子类型，还不能进行种子交易";
                break;
            case 848:
                message = "你怎么跑到别人地里摘桃子啊，主人来了，快跑";
                break;
            case 849:
                message = "主人，你没有领狗被偷的一塌糊涂，快速分享两个农户去领狗吧！";
                break;
            case 850:
                message = "主人，此狗看守能力有限，分享更多好友升级领养一条好狗吧！";
                break;
            case 851:
                message = "主人，藏獒果真名不虚传，帮你击退了大部分来犯之贼，再接再厉，别让好狗流浪了！";
                break;
            case 852:
                message = "你分享的伙伴太少了，偷东西也要团队合作，赶紧去分享吧！";
                break;
            case 854:
                message = "手机号未注册";
                break;
            case 860:
                message = "主人，点击化肥按钮果果立刻帮你施肥，你不会累的！";
                break;
            case 861:
                message = "如用其他在支付手段购买，请耐心等一会，正在兑换积分";
                break;
            case 862:
                message = "提现金额不符合整百提现规则！";
                break;
            case 863:
                message = "您还有未处理的提现记录！";
                break;
            case 864:
                message = "转账积分必须大于100！";
                break;
            case 865:
                message = "自己给自己转账亏你想的出！";
                break;
            case 866:
                message = "嗨，菜鸟，果果帮你看住了大部分种子，快感谢我吧";
                break;
            case 867:
                message = "主人，你活跃值太低，狗狗没能耐得住寂寞，狠心的流浪去了，快分享好友把它找回来吧！";
                break;
            case 868:
                message = "果果已收到您的圣旨，稍后可去粮仓查看是否有惊喜";
                break;
            case 869:
                message = "做人要有底线，这家主人已经发怒了，换别家去偷吧";
                break;
            case 870:
                message = "累了吧，主人，休息两个小时再来吧";
                break;
            case 871:
                message = "主人，您太棒了!分享收益已促动你的种子成熟了，不用出售直接点击种子购买吧！";
                break;
            case 872:
                message = "主人，您已经有足够的种子啦，请查看种子仓库或抢下种子试试";
                break;
            case 873:
                message = "稍后果果会将种子直接送达您的仓库，请耐心等待下哦！";
                break;
            case 874:
                message = "没有生长中的植物土地就浪费了，快去收掉已成熟的种子吧!";
                break;
            case 875:
                message = "表现很好奖励施肥";
                break;
            case 876:
                message = "果果正在帮您出售，5分钟后请注意查收收米！";
                break;
            case 877:
                message = "先把采摘的种子出售并收米再来买种子吧！";
                break;
            case 878:
                message = "您转账的用户不是您的粉丝，不能转账哦！";
                break;
            case 879:
                message = "土地成熟了，先去采摘再来收米吧，被偷可就不好了";
                break;
            case 1000:
                message = "验证码发送失败";
                break;
        }
        return message;
    }
}
