package com.bandit.mshop.others;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class OnScaleTouchListener implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale = false;

    public OnScaleTouchListener (Context c){
        gestureScale = new ScaleGestureDetector(c, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        gestureScale.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = (scaleFactor < 1 ? 1 : scaleFactor); // prevent our view from becoming too small //
        scaleFactor = ((float)((int)(scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) { inScale = false; }
}