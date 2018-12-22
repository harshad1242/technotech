package com.technotech.technotechapplication.util;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by welcome on 29-11-2016.
 */

public class VerticalOnlyNestedScrollView extends NestedScrollView {
    private static final String TAG = "VOnlyNestedScrollView";
    public VerticalOnlyNestedScrollView(Context context) {
        super(context);
    }

    private float startX, startY;

    public VerticalOnlyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalOnlyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!(ev.getAction()== MotionEvent.ACTION_DOWN)&& ev.getHistorySize() > 0) {
            float yVector = ev.getY() - ev.getHistoricalY(0);
            float xVector = ev.getX() - ev.getHistoricalX(0);
            Log.d(TAG, "onInterceptTouchEvent: " + xVector + "--" + yVector);
            return Math.abs(yVector) <= Math.abs(xVector) || super.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }
}