package com.zybooks.rollerball;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScoreSQL extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rollBall.db";
    private static final int VERSION = 2;

    public ScoreSQL(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class SQLTable {
        private static final String TABLE = "timeTable";
        private static final String COL_ID = "_id";
        private static final String COL_AAA = "aaa";
        private static final String COL_Time = "time";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + SQLTable.TABLE + " (" +
                        SQLTable.COL_ID + " integer primary key autoincrement, " +
                        SQLTable.COL_AAA + " text, " +
                        SQLTable.COL_Time + " integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + SQLTable.TABLE);
        onCreate(db);
    }

    public long addTime(long ttime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLTable.COL_AAA, "aaa");
        int timeInt = (int)  ttime;
        values.put(SQLTable.COL_Time, timeInt);

        long scoreID = db.insert(SQLTable.TABLE, null, values);
        return scoreID;
    }

    public List<Long> getData() {
        List<Long> timeList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String sql = "select * from " + SQLTable.TABLE + " where aaa = ? ";
        Cursor cursor = db.rawQuery(sql, new String[] {"aaa"});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String aaa = cursor.getString(1);
                long timeLong = cursor.getLong(2);
                //float stars = cursor.getFloat(3);
                //Log.d(TAG, "???????????????????????????? = " + id + ", " + timeLong);
                timeList.add(timeLong);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return timeList;
    }
}
