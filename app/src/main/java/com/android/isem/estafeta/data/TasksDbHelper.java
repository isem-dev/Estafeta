package com.android.isem.estafeta.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.isem.estafeta.data.TasksContract.TaskEntry;

public class TasksDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Tasks.db";

    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskEntry.COLUMN_TASK_ID + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_NUMBER + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_PLANNED_START_DATE + " TEXT, " +
                TaskEntry.COLUMN_PLANNED_END_DATE + " TEXT, " +
                TaskEntry.COLUMN_ACTUAL_START_DATE + " TEXT, " +
                TaskEntry.COLUMN_ACTUAL_END_DATE + " TEXT, " +
                TaskEntry.COLUMN_VIN + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_MODEL + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_MODEL_CODE + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_BRAND + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_SURVEY_POINT + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_CARRIER + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_DRIVER + " TEXT NOT NULL, " +
                " UNIQUE (" + TaskEntry.COLUMN_TASK_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
