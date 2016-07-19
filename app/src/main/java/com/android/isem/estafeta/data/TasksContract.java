package com.android.isem.estafeta.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TasksContract {

    public static final String CONTENT_AUTHORITY = "com.android.isem.estafeta.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_TASK_ID = "task_id";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_PLANNED_START_DATE = "planned_start_date";
        public static final String COLUMN_PLANNED_END_DATE = "planned_end_date";
        public static final String COLUMN_ACTUAL_START_DATE = "actual_start_date";
        public static final String COLUMN_ACTUAL_END_DATE = "actual_end_date";
        public static final String COLUMN_VIN = "vin";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_MODEL_CODE = "model_code";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_SURVEY_POINT = "survey_point";
        public static final String COLUMN_CARRIER = "carrier";
        public static final String COLUMN_DRIVER = "driver";

        public static final String[] DEFAULT_PROJECTION = {
                TaskEntry.TABLE_NAME + "." + TaskEntry._ID
                , TaskEntry.COLUMN_TASK_ID
                , TaskEntry.COLUMN_NUMBER
                , TaskEntry.COLUMN_PLANNED_START_DATE
                , TaskEntry.COLUMN_PLANNED_END_DATE
                , TaskEntry.COLUMN_ACTUAL_START_DATE
                , TaskEntry.COLUMN_ACTUAL_END_DATE
                , TaskEntry.COLUMN_VIN
                , TaskEntry.COLUMN_MODEL
                , TaskEntry.COLUMN_MODEL_CODE
                , TaskEntry.COLUMN_BRAND
                , TaskEntry.COLUMN_SURVEY_POINT
                , TaskEntry.COLUMN_CARRIER
                , TaskEntry.COLUMN_DRIVER
        };

        public static final int COL_INDEX_TASK_ENTRY_ID = 0;
        public static final int COL_INDEX_TASK_ID = 1;
        public static final int COL_INDEX_NUMBER = 2;
        public static final int COL_INDEX_PLANNED_START_DATE = 3;
        public static final int COL_INDEX_PLANNED_END_DATE = 4;
        public static final int COL_INDEX_ACTUAL_START_DATE = 5;
        public static final int COL_INDEX_ACTUAL_END_DATE = 6;
        public static final int COL_INDEX_VIN = 7;
        public static final int COL_INDEX_MODEL = 8;
        public static final int COL_INDEX_MODEL_CODE = 9;
        public static final int COL_INDEX_BRAND = 10;
        public static final int COL_INDEX_SURVEY_POINT = 11;
        public static final int COL_INDEX_CARRIER = 12;
        public static final int COL_INDEX_DRIVER = 13;

        // Determine URIs
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // Create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final Uri buildTasksUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final Uri buildTaskWithIdUri(int task_id) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(task_id)).build();
        }
    }
}
