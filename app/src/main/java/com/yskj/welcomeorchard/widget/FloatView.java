package com.yskj.welcomeorchard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 创建日期 2017/4/14on 14:16.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class FloatView extends LinearLayout {
    private int lastX,lastY,screenWidth,screenHeight;

    public FloatView(Context context) {
        this(context, null);
    }

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels-50;//减去下边的高度
    }
    //定位
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //可以在这里确定这个viewGroup的：宽 = r-l.高 = b - t
    }
    OnTouchListener onDragTouchListener = new OnTouchListener()
    {
        private float startX = 0;
        private float startY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                }
                case MotionEvent.ACTION_MOVE:
                {
                    // 计算偏移量
                    int dx = (int) (event.getRawX() - startX);
                    int dy = (int) (event.getRawY() - startY);
                    // 计算控件的区域
                    int left = v.getLeft() + dx;
                    int right = v.getRight() + dx;
                    int top = v.getTop() + dy;
                    int bottom = v.getBottom() + dy;
                    // 超出屏幕检测
                    if (left < 0)
                    {
                        left = 0;
                        right = v.getWidth();
                    }
                    if (right > screenWidth)
                    {
                        right = screenWidth;
                        left = screenWidth - v.getWidth();
                    }
                    if (top < 0)
                    {
                        top = 0;
                        bottom = v.getHeight();
                    }
                    if (bottom > screenHeight)
                    {
                        bottom = screenHeight;
                        top = screenHeight - v.getHeight();
                    }
                    v.layout(left, top, right, bottom);
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                }
            }
            return false;
        }
    };
}
