package com.zybooks.rollerball;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class RollerThread extends Thread  {

    private SurfaceHolder mSurfaceHolder;
    private RollerGame mRollerGame;
    private boolean mThreadRunning;
    private PointF mVelocity;

    public RollerThread(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mThreadRunning = true;

        mVelocity = new PointF();

        // Create a ball with boundaries determined by SurfaceView
        Canvas canvas = mSurfaceHolder.lockCanvas();
        mRollerGame = new RollerGame(canvas.getWidth(), canvas.getHeight());
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        try {
            int iR = 0;
            long startTime = SystemClock.uptimeMillis();
            while (mThreadRunning) {
                long cTime = SystemClock.uptimeMillis();
                long duration = (cTime - startTime) / 100;
                Canvas canvas = mSurfaceHolder.lockCanvas();
                mRollerGame.update(mVelocity);
                mRollerGame.draw(canvas, duration);
                //Log.d(TAG, "run: ====>>>>>>>>" + duration);
                //////////////////////////////////////////////////

                mSurfaceHolder.unlockCanvasAndPost(canvas);
                iR ++;
            }
        }
        catch (NullPointerException ex) {
            // In case canvas is destroyed while thread is running
            ex.printStackTrace();
        }
    }

    public void changeAcceleration(float xForce, float yForce) {
        mVelocity.x = xForce;
        mVelocity.y = yForce;
    }

    public void stopThread() {
        mThreadRunning = false;
    }

    public void shake() {
        mRollerGame.newGame();
    }
}