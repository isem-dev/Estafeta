package com.android.isem.estafeta;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.isem.estafeta.data.TasksContract.TaskEntry;

public class TasksAdapter extends CursorAdapter {

    public TasksAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView taskIdView;
        public final TextView actualStartDateView;
        public final TextView brandView;
        public final TextView modelView;
        public final TextView driverView;
        public final TextView numberView;

        public ViewHolder(View view) {
            taskIdView = (TextView) view.findViewById(R.id.task_id_textview);
            actualStartDateView = (TextView) view.findViewById(R.id.actual_start_date_textview);
            brandView = (TextView) view.findViewById(R.id.brand_textview);
            modelView = (TextView) view.findViewById(R.id.model_textview);
            driverView = (TextView) view.findViewById(R.id.driver_textview);
            numberView = (TextView) view.findViewById(R.id.number_textview);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.taskIdView.setText(String.valueOf(cursor.getInt(TaskEntry.COL_INDEX_TASK_ID)));
        viewHolder.actualStartDateView.setText(cursor.getString(TaskEntry.COL_INDEX_ACTUAL_START_DATE));
        viewHolder.brandView.setText(cursor.getString(TaskEntry.COL_INDEX_BRAND));
        viewHolder.modelView.setText(cursor.getString(TaskEntry.COL_INDEX_MODEL));
        viewHolder.driverView.setText(cursor.getString(TaskEntry.COL_INDEX_DRIVER));
        viewHolder.numberView.setText(cursor.getString(TaskEntry.COL_INDEX_NUMBER));
    }
}
