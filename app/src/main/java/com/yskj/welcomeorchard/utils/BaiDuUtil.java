package com.yskj.welcomeorchard.utils;

/**
 * Created by jianghe on 2016/12/1 0001.
 */
public class BaiDuUtil {
    private static final double EARTH_RADIUS = 6377830; //赤道半径(单位m)
    /**
     * 得到两经纬度之间的距离 优化版
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
