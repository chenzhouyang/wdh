package com.yskj.welcomeorchard.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者： 闄堝畽娲�
 * 时间： 2016/9/26.
 */
public class StringUtil {
    public static String getmess(String code){
        String mess = null;
        if(code.equals("1")){
            mess = "参数错误";
        }else if(code.equals("2")){
            mess = "删除收货地址失败";
        }else if(code.equals("3")){
            mess = "更新默认收货地址";
        }else if(code.equals("4")){
            mess = "获取收货地址失败";
        }else if(code.equals("5")){
            mess = "参数为空";
        }else if(code.equals("6")){
            mess = "库存为空";
        }else if(code.equals("7")){
            mess = "密钥错误";
        }else if(code.equals("8")){
            mess = "用户为空";
        }else if(code.equals("0")){
            mess = "操作成功";
        }
        return mess;
    }
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) &&
                codePoint <= 0xD7FF))|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    /**
     * 关键字高亮显示
     *
     * @param context 上下文
     * @param text    需要显示的文字
     * @param target  需要高亮的关键字
     * @param color   高亮颜色
     * @param start   头部增加高亮文字个数
     * @param end     尾部增加高亮文字个数
     * @return 处理完后的结果
     */
    public static SpannableString highlight(Context context, String text, String target,
                                            String color, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}
