package com.zybooks.rollerball;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private RollerSurfaceView mSurfaceView;
    //////////////////////////////////////////
    private final int SHAKE_THRESHOLD = 100;
    private float mLastAcceleration = SensorManager.GRAVITY_EARTH;

    private static MainActivity instance;
    MediaPlayer mMediaPlayer;
    TextView timerTView;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSurfaceView = findViewById(R.id.rollerSurface);

        // For testing in the emulator
        mSurfaceView.setOnClickListener(view -> mSurfaceView.shake());

        //
        mSurfaceView.setOnTouchListener ((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //mInitX = (int) event.getX();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getX();
                    //Log.d(TAG, "onCreate: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    VariableSingle vS = VariableSingle.getInstance();
                    vS.superAbility = true;
                    // See if movement is at least 20 pixels
//                    if (Math.abs(x - mInitX) >= 20) {
//                        if (x > mInitX) {
//                            mDice[0].addOne();
//                        }
//                        else {
//                            mDice[0].subtractOne();
//                        }
//                        showDice();
//                        mInitX = x;
//                    }

                    return true;
            }
            return false;
        });

        instance = this;

        timerTView = findViewById(R.id.timerTViewID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Get accelerometer values
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Move the ball
        mSurfaceView.changeAcceleration(x, y);

        /////////////////////////////////////////
        // Find magnitude of acceleration
        float currentAcceleration = x * x + y * y + z * z;

        // Calculate difference between 2 readings
        float delta = currentAcceleration - mLastAcceleration;

        // Save for next time
        mLastAcceleration = currentAcceleration;

        // Detect shake
        if (Math.abs(delta) > SHAKE_THRESHOLD) {
            mSurfaceView.shake();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    public static MainActivity getInstance() {
        return instance;
    }

    void MPlayerFun(String str) {
        if(mMediaPlayer == null) {
            if (str == "hit") {
                mMediaPlayer = MediaPlayer.create(this, R.raw.disappoint);
            }
            else if (str == "win") {
                mMediaPlayer = MediaPlayer.create(this, R.raw.win_sound);
            }

            mMediaPlayer.start();
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}