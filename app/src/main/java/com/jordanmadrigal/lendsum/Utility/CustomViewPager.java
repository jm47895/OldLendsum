package com.jordanmadrigal.lendsum.Utility;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean swipePageEnabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipePageEnabled = false; // By default swiping is disabled
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.swipePageEnabled && super.onTouchEvent(event);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.swipePageEnabled && super.onInterceptTouchEvent(event);

    }

    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        return this.swipePageEnabled && super.executeKeyEvent(event);
    }

    public void setSwipeEnabled(boolean enabled) {
        this.swipePageEnabled = enabled;
    }

    public boolean isSwipeEnabled(){
        return swipePageEnabled;
    }

    public boolean isFragmentShowing(boolean showing){
        return showing;
    }

}
