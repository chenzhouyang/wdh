package com.yskj.welcomeorchard.utils;

import android.net.ParseException;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Android Studio.
 * 作者: 陈宙洋
 * 日期: 2016/8/12 13:44
 */
public class MobileEncryption {
    //是邮箱
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 隐藏手机号
     *
     * @param mobile
     * @return
     */
    public static String getMobile(String mobile) {
        StringBuilder sb = null;
        if (!TextUtils.isEmpty(mobile) && mobile.length() > 6) {
            sb = new StringBuilder();
            for (int i = 0; i < mobile.length(); i++) {
                char c = mobile.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 隐藏银行卡号
     *
     * @param bank
     * @return
     */
    public static String getBanks(String bank) {

        if (!TextUtils.isEmpty(bank) && bank.length() > 6) {
            String banks = "";
            for (int i = 0; i < bank.length()-4; i++) {
                banks+="*";
            }
            banks+= bank.substring(bank.length()-4,bank.length());
            return banks;
        }

        return bank;

    }
public static String getBank(String bank){
    return bank;

}
    /**
     * 规范Double类型
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double atod(String str) throws Exception {

        boolean negative = false;
        //get the value before the "."
        double valueBeforeDot = 0.0d;
        //get the value after the ".";
        double valueAfterDot = 0.0d;
        boolean pointAppear = false;
        int count = 0;

        //null or empty string
        if (str == null || str.equals("")) {
            throw new Exception("null string or the string has no character!");
        }

        for (int i = 0; i < str.length(); i++) {
            //check whether the first character is "+" or "-"
            if (i == 0 && (str.charAt(0) == '-' || str.charAt(0) == '+')) {
                if (str.charAt(0) == '-') {
                    negative = true;
                    continue;
                }
            }
            //check whether the character is "." and appears for
            //the first time and appears at the correct position.
            if (pointAppear == false && str.charAt(i) == '.') {
                pointAppear = true;
                continue;
            }
            if (str.charAt(i) >= '0' && '9' >= str.charAt(i)) {
                if (pointAppear == false) {
                    valueBeforeDot = valueBeforeDot * 10 + (str.charAt(i) - '0');
                } else {
                    valueAfterDot = valueAfterDot * 10 + (str.charAt(i) - '0');
                    count++;
                }
            } else {
                throw new NumberFormatException("not a double");
            }
        }
        valueBeforeDot = valueBeforeDot + valueAfterDot / Math.pow(10, count);
        return negative == true ? valueBeforeDot * -1 : valueBeforeDot;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
      移动号段：
		134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
		联通号段：
		130 131 132 145 155 156 171 175 176 185 186
	电信号段：
		133 149 153 173 177 180 181 189
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][34587]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 自动生成昵称
     */
    public static String generateNicknameByMobile(String mobile) {
        //手机号为空，返回空值
        /*if(null == mobile|| "".equals(mobile.trim())){
            return null;
        }*/
        //昵称样本数组
        String[] sample = new String[]{"尚客会员", "小主", "云客"};
        //随机数生成器，生成样本数组随机下标
        Random random = new Random();
        int index = random.nextInt(sample.length);

        //拼接昵称前缀和手机号后5位，生成昵称
        StringBuffer nickname = new StringBuffer();
        //如果手机号为空，生成的昵称不带后缀，如果不为空，生成的昵称带手机号后5位
        String suffix = "";
        if (null != mobile && !"".equals(mobile.trim())) {
            int offset = mobile.length() >= 5 ? (mobile.length() - 5) : 0;
            suffix = mobile.substring(offset);
        }
        nickname.append(sample[index]).append(suffix);

        return nickname.toString();
    }


    //身份证号码验证：start

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     * @throws java.text.ParseException
     * @throws NumberFormatException
     */
    public static String IDCardValidate(String IDStr) throws ParseException, NumberFormatException, java.text.ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2","X"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "合法的身份证号";
        }
        // =====================(end)=====================
        return "合法的身份证号";
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    public static boolean getyouxiang(String youxiang) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
        Matcher m = p.matcher(youxiang);
//Mather m = p.matcher("wangxu198709@gmail.com.cn");这种也是可以的！
        boolean b = m.matches();
        return b;
    }
}
