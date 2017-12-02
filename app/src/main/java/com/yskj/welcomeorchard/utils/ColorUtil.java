package com.yskj.welcomeorchard.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YSKJ-JH on 2017/3/13.
 */

public  class ColorUtil {
        /**
         * Get the random color.
         *
         * @return
         */
        public static String getRandomColor() {
            List<String> colorList = new ArrayList<String>();
            colorList.add("#008B00");
            colorList.add("#00C5CD");
            colorList.add("#473C8B");
            colorList.add("#63B8FF");
            colorList.add("#76EEC6");
            colorList.add("#8A2BE2");
            colorList.add("#8B5A00");
            colorList.add("#8B636C");
            colorList.add("#9ACD32");
            colorList.add("#ADADAD");
            colorList.add("#ADFF2F");
            colorList.add("#CD00CD");
            colorList.add("#CD8500");
            colorList.add("#CD8C95");
            colorList.add("#CD3333");
            colorList.add("#CD00CD");
            colorList.add("#DDA0DD");
            colorList.add("#EE9A00");
            colorList.add("#EE82EE");
            colorList.add("#FF00FF");
            colorList.add("#FF6347");
            colorList.add("#FF4500");
            return colorList.get((int) (Math.random() * colorList.size()));
        }
}
