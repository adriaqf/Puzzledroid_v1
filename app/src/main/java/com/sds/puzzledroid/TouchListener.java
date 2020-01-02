package com.sds.puzzledroid;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class TouchListener implements View.OnTouchListener{

    private float xDelta;
    private float yDelta;
    private PuzzleLevelActivity puzzleLevelActivity;

    public TouchListener(PuzzleLevelActivity puzzleLevelActivity) {
        this.puzzleLevelActivity = puzzleLevelActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getRawX();
        float y = event.getRawY();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                v.getLayoutParams();

        switch(event.getAction() & event.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - layoutParams.leftMargin;
                yDelta = y - layoutParams.topMargin;
                break;

            case MotionEvent.ACTION_MOVE:
                layoutParams.leftMargin = (int) (x - xDelta);
                layoutParams.topMargin = (int) (y - yDelta);
                v.setLayoutParams(layoutParams);
                break;
        }

        return true;
    }
}
