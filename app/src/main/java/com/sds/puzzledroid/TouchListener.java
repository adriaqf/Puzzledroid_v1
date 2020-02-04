package com.sds.puzzledroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sds.puzzledroid.activities.JigsawActivity;
import com.sds.puzzledroid.logic.PuzzlePiece;

import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class TouchListener implements View.OnTouchListener {

    private float xDelta, yDelta;
    private JigsawActivity jigsawActivity;
    SoundPool sp;
    int sound_piece;

    public TouchListener(JigsawActivity jigsawActivity) {
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_piece = sp.load(jigsawActivity,R.raw.piece,1);
        this.jigsawActivity = jigsawActivity;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final double tolerance = sqrt(v.getWidth() * v.getHeight())/10; // (1/10) of the piece's square root
        PuzzlePiece piece = (PuzzlePiece) v;


        //The piece is already in the right jigsaw's position
        if (!piece.canMove) {
                return true;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            //Saves piece's initial place
            case MotionEvent.ACTION_DOWN:
                xDelta = event.getRawX() - layoutParams.leftMargin;
                yDelta = event.getRawY() - layoutParams.topMargin;
                piece.bringToFront();
                break;

            //Set margins where the piece can move
            case MotionEvent.ACTION_MOVE:
                layoutParams.leftMargin = (int) (event.getRawX() - xDelta);
                layoutParams.topMargin = (int) (event.getRawY() - yDelta);
                layoutParams.rightMargin = -((PuzzlePiece) v).pieceWidth;
                layoutParams.bottomMargin = -((PuzzlePiece) v).pieceHeight;
                v.setLayoutParams(layoutParams);
                break;

            //Place the piece in the puzzle if it is equal or within the tolerance value
            case MotionEvent.ACTION_UP:
                int xDifference = abs(piece.xCoord - layoutParams.leftMargin);
                int yDifference = abs(piece.yCoord - layoutParams.topMargin);

                if (xDifference <= tolerance && yDifference <= tolerance) {
                    layoutParams.leftMargin = piece.xCoord;
                    layoutParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(layoutParams);
                    piece.canMove = false;
                    soundPoolPiece();
                    Toast.makeText(jigsawActivity,"Pausa",Toast.LENGTH_SHORT).show();
                    sendViewToBack(piece);
                    jigsawActivity.checkGameOver();
                }
                break;
        }

        return true;
    }

    private void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }
    public void soundPoolPiece(){
        SharedPreferences pref = jigsawActivity.getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound",true);

        if(value){
            sp.play(sound_piece,1,1,1,0,0);
        }

    }

}