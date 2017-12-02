package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/22.
 */

public class BuyersShowEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * page : {"firstRow":0,"listRows":5,"parameter":{"sk":"2ab99a6af22365686e97992df974d5c2","gid":"140","p":"1"},"totalRows":"10","totalPages":2,"rollPage":11,"lastSuffix":true,"p":"p","url":"","nowPage":1}
     * commentlist : [{"comment_id":"374","goods_id":"154","email":"","username":null,"content":"12112","deliver_rank":"5","add_time":"1488197599","ip_address":"192.168.0.2","is_show":"1","parent_id":"0","user_id":"149","img":["/Public/upload/comment/2017-02-27/58b417de85aaa.jpg"],"order_id":null,"goods_rank":"5","service_rank":"5"},{"comment_id":"373","goods_id":"151","email":"","username":null,"content":"gf","deliver_rank":"5","add_time":"1487323466","ip_address":"192.168.0.33","is_show":"1","parent_id":"0","user_id":"37","img":["/Public/upload/comment/2017-02-17/58a6c14a3ab31.jpeg"],"order_id":null,"goods_rank":"5","service_rank":"5"},{"comment_id":"339","goods_id":"140","email":"","username":"尚客会员87139","content":"性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高价比蛮高","deliver_rank":"4","add_time":"1484712152","ip_address":"127.0.0.1","is_show":"1","parent_id":"0","user_id":"32","img":["/Public/upload/comment/2017/01-18/587ee8b3960c9.jpg","/Public/upload/comment/2017/01-18/587ee8c5716d9.png"],"order_id":"307","goods_rank":"4","service_rank":"2"},{"comment_id":"332","goods_id":"142","email":"","username":"梅梅","content":"hhhh","deliver_rank":"4","add_time":"1478574675","ip_address":"127.0.0.1","is_show":"1","parent_id":"0","user_id":"203","img":["/Public/upload/comment/2016-11-08/58214253a2eb1.jpg"],"order_id":"47","goods_rank":"4","service_rank":"3"},{"comment_id":"331","goods_id":"142","email":"","username":"梅梅","content":"非常好","deliver_rank":"5","add_time":"1478573520","ip_address":"192.168.0.135","is_show":"1","parent_id":"0","user_id":"203","img":false,"order_id":"48","goods_rank":"4","service_rank":"3"}]
     * replyList : [{"comment_id":"328","goods_id":"140","username":"admin","content":"谢谢您的评价，谢谢您的评价，谢谢您的评价","add_time":"1477471865"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("page")
    public PageBean page;
    @SerializedName("commentlist")
    public ArrayList<CommentlistBean> commentlist;
    @SerializedName("replyList")
    public ArrayList<ReplyListBean> replyList;

    public static class PageBean {
        /**
         * firstRow : 0
         * listRows : 5
         * parameter : {"sk":"2ab99a6af22365686e97992df974d5c2","gid":"140","p":"1"}
         * totalRows : 10
         * totalPages : 2
         * rollPage : 11
         * lastSuffix : true
         * p : p
         * url :
         * nowPage : 1
         */

        @SerializedName("firstRow")
        public int firstRow;
        @SerializedName("listRows")
        public int listRows;
        @SerializedName("parameter")
        public ParameterBean parameter;
        @SerializedName("totalRows")
        public String totalRows;
        @SerializedName("totalPages")
        public int totalPages;
        @SerializedName("rollPage")
        public int rollPage;
        @SerializedName("lastSuffix")
        public boolean lastSuffix;
        @SerializedName("p")
        public String p;
        @SerializedName("url")
        public String url;
        @SerializedName("nowPage")
        public int nowPage;

        public static class ParameterBean {
            /**
             * sk : 2ab99a6af22365686e97992df974d5c2
             * gid : 140
             * p : 1
             */

            @SerializedName("sk")
            public String sk;
            @SerializedName("gid")
            public String gid;
            @SerializedName("p")
            public String p;
        }
    }

    public static class CommentlistBean {
        /**
         * comment_id : 374
         * goods_id : 154
         * email :
         * username : null
         * content : 12112
         * deliver_rank : 5
         * add_time : 1488197599
         * ip_address : 192.168.0.2
         * is_show : 1
         * parent_id : 0
         * user_id : 149
         * img : ["/Public/upload/comment/2017-02-27/58b417de85aaa.jpg"]
         * order_id : null
         * goods_rank : 5
         * service_rank : 5
         */

        @SerializedName("comment_id")
        public String commentId;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("email")
        public String email;
        @SerializedName("username")
        public String username;
        @SerializedName("content")
        public String content;
        @SerializedName("deliver_rank")
        public String deliverRank;
        @SerializedName("add_time")
        public String addTime;
        @SerializedName("ip_address")
        public String ipAddress;
        @SerializedName("is_show")
        public String isShow;
        @SerializedName("parent_id")
        public String parentId;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("goods_rank")
        public String goodsRank;
        @SerializedName("service_rank")
        public String serviceRank;
        @SerializedName("img")
        public ArrayList<String> img;
    }

    public static class ReplyListBean {
        /**
         * comment_id : 328
         * goods_id : 140
         * username : admin
         * content : 谢谢您的评价，谢谢您的评价，谢谢您的评价
         * add_time : 1477471865
         */

        @SerializedName("comment_id")
        public String commentId;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("username")
        public String username;
        @SerializedName("content")
        public String content;
        @SerializedName("add_time")
        public String addTime;
    }

//    /**
//     * error_code : 000
//     * error_msg : SUCCESS
//     * commentlist : [{"comment_id":"339","goods_id":"140","email":"","username":"尚客会员87139","content":"性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的","deliver_rank":"4","add_time":"1484712152","ip_address":"127.0.0.1","is_show":"1","parent_id":"0","user_id":"32","img":["/Public/upload/comment/2017/01-18/587ee8b3960c9.jpg","/Public/upload/comment/2017/01-18/587ee8c5716d9.png"],"order_id":"307","goods_rank":"4","service_rank":"2"},{"comment_id":"327","goods_id":"140","email":"","username":"尚客会员87139","content":"不还错吧，跟实体店一样,不还错吧，跟实体店一样,不还错吧，跟实体店一样","deliver_rank":"4","add_time":"1477471314","ip_address":"127.0.0.1","is_show":"1","parent_id":"0","user_id":"32","img":["/Public/upload/comment/2016/10-26/58106c3c2835e.png"],"order_id":"46","goods_rank":"5","service_rank":"5"}]
//     * replyList : [{"comment_id":"328","goods_id":"140","username":"admin","content":"谢谢您的评价，谢谢您的评价，谢谢您的评价","add_time":"1477471865"}]
//     */
//
//    @SerializedName("error_code")
//    public String errorCode;
//    @SerializedName("error_msg")
//    public String errorMsg;
//    @SerializedName("page")
//    public Page page;
//    @SerializedName("commentlist")
//    public ArrayList<CommentlistBean> commentlist;
//    @SerializedName("replyList")
//    public ArrayList<ReplyListBean> replyList;
//
//    public class Page{
//        @SerializedName("firstRow")
//        public String firstRow;
//        @SerializedName("listRows")
//        public String listRows;
//        @SerializedName("totalRows")
//        public String totalRows;
//        @SerializedName("totalPages")
//        public String totalPages;
//        @SerializedName("rollPage")
//        public String rollPage;
//        @SerializedName("lastSuffix")
//        public boolean lastSuffix;
//        @SerializedName("p")
//        public String p;
//        @SerializedName("url")
//        public String url;
//        @SerializedName("nowPage")
//        public String nowPage;
//        @SerializedName("parameter")
//        public Paramet parameter;
//    }
//
//    public class Paramet{
//        @SerializedName("p")
//        public String p;
//        @SerializedName("gid")
//        public String gid;
//        @SerializedName("sk")
//        public String sk;
//    }
//
//    public class CommentlistBean {
//        /**
//         * comment_id : 339
//         * goods_id : 140
//         * email :
//         * username : 尚客会员87139
//         * content : 性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的,性价比蛮高的
//         * deliver_rank : 4
//         * add_time : 1484712152
//         * ip_address : 127.0.0.1
//         * is_show : 1
//         * parent_id : 0
//         * user_id : 32
//         * img : ["/Public/upload/comment/2017/01-18/587ee8b3960c9.jpg","/Public/upload/comment/2017/01-18/587ee8c5716d9.png"]
//         * order_id : 307
//         * goods_rank : 4
//         * service_rank : 2
//         */
//
//        @SerializedName("comment_id")
//        public String commentId;
//        @SerializedName("goods_id")
//        public String goodsId;
//        @SerializedName("email")
//        public String email;
//        @SerializedName("username")
//        public String username;
//        @SerializedName("content")
//        public String content;
//        @SerializedName("deliver_rank")
//        public String deliverRank;
//        @SerializedName("add_time")
//        public String addTime;
//        @SerializedName("ip_address")
//        public String ipAddress;
//        @SerializedName("is_show")
//        public String isShow;
//        @SerializedName("parent_id")
//        public String parentId;
//        @SerializedName("user_id")
//        public String userId;
//        @SerializedName("order_id")
//        public String orderId;
//        @SerializedName("goods_rank")
//        public String goodsRank;
//        @SerializedName("service_rank")
//        public String serviceRank;
//        @SerializedName("img")
//        public ArrayList<String> img;
//    }
//
//    public class ReplyListBean {
//        /**
//         * comment_id : 328
//         * goods_id : 140
//         * username : admin
//         * content : 谢谢您的评价，谢谢您的评价，谢谢您的评价
//         * add_time : 1477471865
//         */
//
//        @SerializedName("comment_id")
//        public String commentId;
//        @SerializedName("goods_id")
//        public String goodsId;
//        @SerializedName("username")
//        public String username;
//        @SerializedName("content")
//        public String content;
//        @SerializedName("add_time")
//        public String addTime;
//    }
}
