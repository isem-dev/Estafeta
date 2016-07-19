package com.android.isem.estafeta.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.isem.estafeta.data.TasksContract.TaskEntry;

public class TasksProvider extends ContentProvider {

    private static final int TASKS = 100;
    private static final int TASK_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private TasksDbHelper tasksDbHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TasksContract.CONTENT_AUTHORITY;

        // content://com.android.isem.estafeta.app/tasks
        matcher.addURI(authority, TaskEntry.TABLE_NAME, TASKS);

        // content://com.android.isem.estafeta.app/tasks/id
        matcher.addURI(authority, TaskEntry.TABLE_NAME + "/#", TASK_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        tasksDbHelper = new TasksDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor resultCursor;
        final int match = uriMatcher.match(uri);

        switch (match) {
            case TASKS: {
                resultCursor = tasksDbHelper.getReadableDatabase().query(
                        TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TASK_WITH_ID: {
                resultCursor = tasksDbHelper.getReadableDatabase().query(
                        TaskEntry.TABLE_NAME,
                        projection,
                        TaskEntry.COLUMN_TASK_ID + " = ? ",
                        new String[] {uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return resultCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case TASKS: return TaskEntry.CONTENT_DIR_TYPE;
            case TASK_WITH_ID: return TaskEntry.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = tasksDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASKS: {
                long _id = db.insert(TaskEntry.TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = TaskEntry.buildTasksUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = tasksDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        // This makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case TASKS: {
                rowsDeleted = db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = tasksDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TASKS: {
                rowsUpdated = db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = tasksDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match) {
            case TASKS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TaskEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    // If we do not set the transaction to be successful,
                    // the records will not be committed when we call endTransaction()
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return returnCount;
            }
            default: return super.bulkInsert(uri, values);
        }
    }
}
