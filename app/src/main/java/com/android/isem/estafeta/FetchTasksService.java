package com.android.isem.estafeta;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.android.isem.estafeta.data.TasksContract.TaskEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class FetchTasksService extends IntentService {

    private final String LOG_TAG = FetchTasksService.class.getSimpleName();

    public FetchTasksService() {
        super("FetchTasksService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String tasksJsonStr;

        //TODO Basic Authentication using HttpUrlConnection
        try {
            final String TASKS_BASE_URL = "http://amt2.estafeta.org/api/mobilesurveytasks/gettestsurveytasks";
            final String LOGIN = "admin";
            final String PASS = "1";
            final String COMPANY_ID = "9F346DDB-8FF8-4F42-8221-6E03D6491756";

            URL url = new URL(TASKS_BASE_URL);
            String authString = LOGIN + "@" + COMPANY_ID + ":" + PASS;
            String encodedAuthStr = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization", "Basic " + encodedAuthStr);

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                Log.v(LOG_TAG, "Stream was empty, no point in parsing");
                return;
            }

            tasksJsonStr = buffer.toString();
            Log.d(LOG_TAG, "Tasks JSON String: " + tasksJsonStr);

            getTasksDataFromJsonStr(tasksJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void getTasksDataFromJsonStr(String tasksJsonStr) throws JSONException {
        final String TASK_ID = "Id";
        final String NUMBER = "Number";
        final String PLANNED_START_DATE = "PlannedStartDate";
        final String PLANNED_END_DATE = "PlannedEndDate";
        final String ACTUAL_START_DATE = "ActualStartDate";
        final String ACTUAL_END_DATE = "ActualEndDate";
        final String VIN = "Vin";
        final String MODEL = "Model";
        final String MODEL_CODE = "ModelCode";
        final String BRAND = "Brand";
        final String SURVEY_POINT = "SurveyPoint";
        final String CARRIER = "Carrier";
        final String DRIVER = "Driver";

        try {
            JSONArray tasksArray = new JSONArray(tasksJsonStr);

            Vector<ContentValues> contentValuesVector = new Vector<>(tasksArray.length());
            for(int i = 0; i < tasksArray.length(); i++) {
                JSONObject taskObject = tasksArray.getJSONObject(i);
                ContentValues taskValues = new ContentValues();
                taskValues.put(TaskEntry.COLUMN_TASK_ID, taskObject.getInt(TASK_ID));
                taskValues.put(TaskEntry.COLUMN_NUMBER, taskObject.getString(NUMBER));
                taskValues.put(TaskEntry.COLUMN_PLANNED_START_DATE, taskObject.getString(PLANNED_START_DATE));
                taskValues.put(TaskEntry.COLUMN_PLANNED_END_DATE, taskObject.getString(PLANNED_END_DATE));
                taskValues.put(TaskEntry.COLUMN_ACTUAL_START_DATE, taskObject.getString(ACTUAL_START_DATE));
                taskValues.put(TaskEntry.COLUMN_ACTUAL_END_DATE, taskObject.getString(ACTUAL_END_DATE));
                taskValues.put(TaskEntry.COLUMN_VIN, taskObject.getString(VIN));
                taskValues.put(TaskEntry.COLUMN_MODEL, taskObject.getString(MODEL));
                taskValues.put(TaskEntry.COLUMN_MODEL_CODE, taskObject.getString(MODEL_CODE));
                taskValues.put(TaskEntry.COLUMN_BRAND, taskObject.getString(BRAND));
                taskValues.put(TaskEntry.COLUMN_SURVEY_POINT, taskObject.getString(SURVEY_POINT));
                taskValues.put(TaskEntry.COLUMN_CARRIER, taskObject.getString(CARRIER));
                taskValues.put(TaskEntry.COLUMN_DRIVER, taskObject.getString(DRIVER));
                contentValuesVector.add(taskValues);
            }

            int inserted = 0;
            if (contentValuesVector.size() > 0) {
                ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
                contentValuesVector.toArray(contentValuesArray);
                inserted = this.getContentResolver()
                        .bulkInsert(TaskEntry.CONTENT_URI, contentValuesArray);
            }
            Log.d(LOG_TAG, "FetchTasksService completed. " + inserted + " rows inserted into DB");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
