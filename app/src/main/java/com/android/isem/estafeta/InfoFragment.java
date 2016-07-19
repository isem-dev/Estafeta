package com.android.isem.estafeta;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.isem.estafeta.data.TasksContract.TaskEntry;

public class InfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private static final int TASK_LOADER_ID = 2;

    private Cursor mDataCursor;
    private TextView taskIdView;
    private TextView numberView;
    private TextView plannedStartDateView;
    private TextView plannedEndDateView;
    private TextView actualStartDateView;
    private TextView actualEndDateView;
    private TextView vinView;
    private TextView modelView;
    private TextView modelCodeView;
    private TextView brandView;
    private TextView surveyPointView;
    private TextView carrierView;
    private TextView driverView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        taskIdView = (TextView) rootView.findViewById(R.id.task_id_textview_info);
        numberView = (TextView) rootView.findViewById(R.id.number_textview_info);
        plannedStartDateView = (TextView) rootView.findViewById(R.id.planned_start_date_textview_info);
        plannedEndDateView = (TextView) rootView.findViewById(R.id.planned_end_date_textview_info);
        actualStartDateView = (TextView) rootView.findViewById(R.id.actual_start_date_textview_info);
        actualEndDateView = (TextView) rootView.findViewById(R.id.actual_end_date_textview_info);
        vinView = (TextView) rootView.findViewById(R.id.vin_textview_info);
        modelView = (TextView) rootView.findViewById(R.id.model_textview_info);
        modelCodeView = (TextView) rootView.findViewById(R.id.model_code_textview_info);
        brandView = (TextView) rootView.findViewById(R.id.brand_textview_info);
        surveyPointView = (TextView) rootView.findViewById(R.id.survey_point_textview_info);
        carrierView = (TextView) rootView.findViewById(R.id.carrier_textview_info);
        driverView = (TextView) rootView.findViewById(R.id.driver_textview_info);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                TaskEntry.DEFAULT_PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDataCursor = data;

        if (mDataCursor != null && mDataCursor.moveToFirst()) {
            taskIdView.setText(String.valueOf(mDataCursor.getInt(TaskEntry.COL_INDEX_TASK_ID)));
            numberView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_NUMBER));
            plannedStartDateView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_PLANNED_START_DATE));
            plannedEndDateView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_PLANNED_END_DATE));
            actualStartDateView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_ACTUAL_START_DATE));
            actualEndDateView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_ACTUAL_END_DATE));
            vinView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_VIN));
            modelView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_MODEL));
            modelCodeView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_MODEL_CODE));
            brandView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_BRAND));
            surveyPointView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_SURVEY_POINT));
            carrierView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_CARRIER));
            driverView.setText(mDataCursor.getString(TaskEntry.COL_INDEX_DRIVER));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
