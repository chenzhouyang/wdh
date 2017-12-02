package com.yskj.welcomeorchard.utils;

/**
 * Created by YSKJ-02 on 2016/12/24.
 */

public class Level {
 public static String geterr_code(int level){
     String err_messge = null;
     switch (level){
         case 0:
             err_messge = "消费者";
             break;
        /* case -1:
             err_messge = "V导购";
             break;
         case 1:
             err_messge = "导购员";
             break;
         case 2:
             err_messge = "经销商";
             break;
         case 3:
             err_messge = "分销商";
             break;
         case 4:
             err_messge = "渠道商";
             break;*/
         default:
             err_messge = "导购员";
             break;
     }
     return err_messge;
 }
}
