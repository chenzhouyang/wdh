package com.yskj.welcomeorchard.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: 计算类
 * 作者: wanglei
 * 日期: 2017/1/16 9:32
 */
public class DoubleUtils {

    private static final int roundingMode = BigDecimal.ROUND_DOWN;
    private static final int defaultScale = 2;

    /**
     * 功能描述: 进行加法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return the double
     * @author wanglei
     * @date 2016-08-06 16:18:54
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).setScale(defaultScale, roundingMode).doubleValue();
    }

    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        return value1.add(value2);
    }

    public static BigDecimal addAndScale(BigDecimal value1, BigDecimal value2) {
        return value1.add(value2).setScale(defaultScale, roundingMode);
    }
/*public static double douin(double value){
    value = Math.ceil(value*100)/100;
    return value;
}*/
    /**
     * 功能描述: 进行减法运算
     *
     * @param value1 被减数
     * @param value2 减数
     * @return double
     * @author wanglei
     * @date 2016 -08-06 16:19:38
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).setScale(defaultScale, roundingMode).doubleValue();
    }

    public static BigDecimal sub(BigDecimal value1, BigDecimal value2) {
        return value1.setScale(defaultScale, roundingMode).subtract(value2);
    }

    public static BigDecimal subAndScale(BigDecimal value1, BigDecimal value2) {
        return value1.subtract(value2).setScale(defaultScale, roundingMode);
    }

    /**
     * 功能描述: 进行乘法运算
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return double
     * @author wanglei
     * @date 2016 -08-06 16:20:20
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal mul(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2);
    }

    public static BigDecimal mulAndScale(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2).setScale(defaultScale, roundingMode);
    }

    /**
     * 功能描述: 进行除法运算
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return double
     * @author wanglei
     * @date 2016 -08-06 16:20:42
     */
    public static double div(double value1, double value2, int scale) {
        if (scale < 0) {
            scale = defaultScale;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.divide(b2, scale, roundingMode).doubleValue();
    }

    /**
     * 功能描述: 不进行四舍五入 只保留小数点后2位
     *
     * @param value the value
     * @return double
     * @author wanglei
     * @date 2016 -08-06 16:24:08
     */
    public static double scale(double value) {
        BigDecimal decimal = new BigDecimal(Double.toString(value));
        return decimal.setScale(defaultScale, roundingMode).doubleValue();
    }

    public static double scale(double value, int scale) {
        BigDecimal decimal = new BigDecimal(Double.toString(value));
        return decimal.setScale(scale, roundingMode).doubleValue();
    }

    /**
     * 功能描述: 不进行四舍五入 只保留小数点后2位
     *
     * @param value the value
     * @return big scale
     * @author wanglei
     * @date 2016 -08-06 16:24:12
     */
    public static BigDecimal scale(BigDecimal value) {
        return value.setScale(defaultScale, roundingMode);
    }

    public static BigDecimal scale(BigDecimal value, int scale) {
        if (scale < 0) {
            scale = defaultScale;
        }
        return value.setScale(scale, roundingMode);
    }

    /**
     * 功能描述: 2个double比较大小
     * 结果等于 0 value1 = value2
     * 结果大于 0 value1 > value2
     * 结果小于 0 value1 < value2
     * <p>
     * 作者: wanglei
     * 日期: 2016-10-15 16:05:19
     */
    public static int doubleCompare(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.compareTo(b2);
    }

    public static String toDecimalString2(double value) {
        String formatStr = "#0.00";
        return new DecimalFormat(formatStr).format(value);
    }

    private static final double EARTH_RADIUS = 6377830; //赤道半径(单位m)

    /**
     * 功能描述: 算出 distance km 所对应的经纬度范围
     * <p>
     * 作者: wanglei
     * 日期: 2016-11-12 13:33:18
     */
    public static Map<String, Double> getRectangle(double myLng, double myLat, double distance) {
        double range = Math.toDegrees(distance) / (EARTH_RADIUS / 1000); //里面的 1 就代表搜索 1km 之内，单位km
        double lngR = range / Math.cos(Math.toRadians(myLat));
        double maxLat = myLat + range;
        double minLat = myLat - range;
        double maxLng = myLng + lngR;
        double minLng = myLng - lngR;
        Map<String, Double> ret = new HashMap<String, Double>();
        ret.put("maxLat", maxLat);
        ret.put("minLat", minLat);
        ret.put("maxLng", maxLng);
        ret.put("minLng", minLng);
        return ret;
    }

    /**
     * 得到两经纬度之间的距离 优化版
     *
     * @param lng1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lng2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double getDistanceSimplify(double lng1, double lat1, double lng2, double lat2) {
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        double Lx = Math.toRadians(dx) * EARTH_RADIUS * Math.cos(Math.toRadians(b)); // 东西距离
        double Ly = EARTH_RADIUS * Math.toRadians(dy); // 南北距离
        return Math.sqrt(Lx * Lx + Ly * Ly);  //用平面的矩形对角距离公式计算总距离
    }
}
