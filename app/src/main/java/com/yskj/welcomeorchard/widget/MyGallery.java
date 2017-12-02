package com.yskj.welcomeorchard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by YSKJ-JH on 2017/1/22.
 */

public class MyGallery extends Gallery {
    public MyGallery(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TOD Auto-generated constructor stub
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int keyCode;
        if (isScrollingLeft(e1, e2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(keyCode, null);
        return true;
    }
}
