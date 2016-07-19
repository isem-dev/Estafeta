package com.android.isem.estafeta;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.isem.estafeta.data.TasksContract.TaskEntry;

public class MainGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASKS_LOADER_ID = 1;

    private TasksAdapter tasksAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchTasks();
    }

    private void fetchTasks() {
        Intent intent = new Intent(getActivity(), FetchTasksService.class);
        getActivity().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_grid, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.main_gridview);

        tasksAdapter = new TasksAdapter(getActivity(), null, 0);

        if (gridView != null) {
            gridView.setAdapter(tasksAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    if (cursor != null) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class)
                                .setData(TaskEntry.buildTaskWithIdUri(cursor.getInt(TaskEntry.COL_INDEX_TASK_ID)));
                        startActivity(intent);
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TASKS_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = TaskEntry.CONTENT_URI;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        return new CursorLoader(
                getActivity(),
                uri,
                TaskEntry.DEFAULT_PROJECTION,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tasksAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tasksAdapter.swapCursor(null);
    }
}
