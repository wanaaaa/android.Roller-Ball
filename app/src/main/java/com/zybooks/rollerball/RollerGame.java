package com.zybooks.rollerball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;



public class RollerGame {

    public final int NUM_WALLS = 3;

    private Ball mBall;
    private ArrayList<Wall> mWalls;
    private WallCustom wallCustom;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private Paint mPaint;
    private boolean mGameOver;
    private Random mRandom;


    public RollerGame(int surfaceWidth, int surfaceHeight) {
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;

        mRandom = new Random();

        // For drawing text
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(90);
        mPaint.setColor(Color.RED);

        mBall = new Ball(mSurfaceWidth, mSurfaceHeight);

        int wallY = mSurfaceHeight / (NUM_WALLS + 1);
        mWalls = new ArrayList<>();

        // Add walls at random locations, and alternate initial direction
        for (int c = 1; c <= NUM_WALLS; c++) {
            boolean initialRight = c % 2 == 0;
            mWalls.add(new Wall(mRandom.nextInt(mSurfaceWidth), wallY * c,
                    initialRight, mSurfaceWidth, mSurfaceHeight));
        }
        boolean initialRight = 3 % 2 == 0;
        wallCustom = new WallCustom(mRandom.nextInt(mSurfaceWidth), wallY * 3,
                initialRight, mSurfaceWidth, mSurfaceHeight);
        newGame();
    }

    public void newGame() {
        mGameOver = false;

        // Reset ball at the top of the screen
        mBall.setCenter(mSurfaceWidth / 2, mBall.RADIUS + 10);

        // Reset walls at random spots
        int i = 1;
        for (Wall wall: mWalls) {
            wall.relocate(mRandom.nextInt(mSurfaceWidth));
            i ++;
        }
    }

    public void update(PointF velocity) {

        if (mGameOver) return;
        int wI = 1;
        // Move ball and walls
        mBall.move(velocity);
        for (Wall wall : mWalls) {
            if (wI == 3) {
                wallCustom.move();
            } else {
                wall.move();
            }

            wI ++;
        }

        // Check for collision
        VariableSingle vSing = VariableSingle.getInstance();
        if(!vSing.superAbility) {
            for (Wall wall : mWalls) {
                if (mBall.intersects(wall)) {
                    mGameOver = true;
                    MainActivity.getInstance().MPlayerFun("hit");
                    ////////>>>>>>>>>>>>>>>>>>>///////////////////////////////
                    vSing.hitOrNot = true;
                }
            }
        } else {
            int cInt = 1;
            for (Wall wall : mWalls) {
                if (cInt != 3) {
                    if (mBall.intersects(wall)) {
                        mGameOver = true;
                        MainActivity.getInstance().MPlayerFun("hit");
                        ////////>>>>>>>>>>>>>>>>>>>///////////////////////////////
                        vSing.hitOrNot = true;
                    }
                }
                cInt ++;
            }
        }


        // Check for win
        if (mBall.getBottom() >= mSurfaceHeight) {
            MainActivity.getInstance().MPlayerFun("win");
            mGameOver = true;
        }
    }


    public void draw(Canvas canvas, long iI) {

        // Wipe canvas clean
        canvas.drawColor(Color.WHITE);

        // Draw ball and walls
        mBall.draw(canvas);
        int wanI = 1;
        for (Wall wall : mWalls) {
            if (wanI == 3 ) {
                wallCustom.draw(canvas);
            } else {
                wall.draw(canvas);
            }

            wanI ++;
        }

        // User win?
        if (mBall.getBottom() >= mSurfaceHeight) {
            String text = "You won!";
            Rect textBounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, mSurfaceWidth / 2f - textBounds.exactCenterX(),
                    mSurfaceHeight / 2f - textBounds.exactCenterY(), mPaint);
        }

        VariableSingle vS = VariableSingle.getInstance();
        if (!vS.hitOrNot) {
            vS.timeLong = iI;

        } else {
            //////////////////////////
            vS = VariableSingle.getInstance();
            if (vS.newGame) {
                //vS.scoreSQL.getData();
                long saveSQL = vS.scoreSQL.addTime(vS.timeLong);
                vS.newGame = false;
            }

            ////////////////////////////////
        }
        String tString = String.valueOf(vS.timeLong) + " 1/100 sec";
        Rect tBounds = new Rect();
        Paint pt = new Paint();
        pt.setColor(Color.BLACK);
        pt.setTextSize(100);

        canvas.drawText(tString, 15, 70, pt);
    }
}