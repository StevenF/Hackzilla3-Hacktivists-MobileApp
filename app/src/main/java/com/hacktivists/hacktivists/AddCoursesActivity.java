package com.hacktivists.hacktivists;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hacktivists.hacktivists.Config.StringConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCoursesActivity extends AppCompatActivity {

    TextInputLayout til_addSubject, til_addLocation, til_addDescription, til_addMinimum, til_addMaximum;
    TextInputEditText tet_addSubject, tet_addLocation, tet_addDescription, tet_addMinimum, tet_addMaximum;


    Button btn_submitPetition;

    Spinner spinner_level;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);

        til_addSubject = findViewById(R.id.til_addSubject);
        til_addLocation = findViewById(R.id.til_addLocation);
        til_addDescription = findViewById(R.id.til_addDescription);
        til_addMinimum = findViewById(R.id.til_addMinimum);
        til_addMaximum = findViewById(R.id.til_addMaximum);

        tet_addSubject = findViewById(R.id.tet_addSubject);
        tet_addLocation = findViewById(R.id.tet_addLocation);
        tet_addDescription = findViewById(R.id.tet_addDescription);
        tet_addMinimum = findViewById(R.id.tet_addMinimum);
        tet_addMaximum = findViewById(R.id.tet_addMaximum);

        btn_submitPetition = findViewById(R.id.btn_submitPetition);

        spinner_level = findViewById(R.id.spinner_level);

        requestQueue = Volley.newRequestQueue(this);

        btn_submitPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidField()) {
                    volleyPostToServer();
                }
            }
        });

        initSpinner();

        onTouchListeners(til_addSubject, tet_addSubject);
        onTouchListeners(til_addLocation, tet_addLocation);
        onTouchListeners(til_addDescription, tet_addDescription);
        onTouchListeners(til_addMinimum, tet_addMinimum);
        onTouchListeners(til_addMaximum, tet_addMaximum);

    }

    private void initSpinner() {
        String[] level = new String[]{"Beginner", "Intermediate", "Advanced"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, level);
        spinner_level.setAdapter(arrayAdapter);
    }

    private void onTouchListeners(final TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        textInputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                textInputLayout.setErrorEnabled(false);

                return false;
            }
        });

    }

    private boolean isValidField() {
        String subject = tet_addSubject.getText().toString();
        String location = tet_addLocation.getText().toString();
        String description = tet_addDescription.getText().toString();
        String minimum = tet_addMinimum.getText().toString();
        String maximum = tet_addMaximum.getText().toString();

        boolean valid = false;

        if (!subject.isEmpty() || !location.isEmpty() || !description.isEmpty() || !maximum.isEmpty() || !minimum.isEmpty()) {
            valid = true;
        }
        if (subject.isEmpty()) {
            til_addSubject.setErrorEnabled(true);
            til_addSubject.setError("This field is required.");
        }

        if (location.isEmpty()) {
            til_addLocation.setErrorEnabled(true);
            til_addLocation.setError("This field is required.");
        }

        if (description.isEmpty()) {
            til_addDescription.setErrorEnabled(true);
            til_addDescription.setError("This field is required.");
        }

        if (minimum.isEmpty()) {
            til_addMinimum.setErrorEnabled(true);
            til_addMinimum.setError("This field is required.");
        }

        if (maximum.isEmpty()) {
            til_addMaximum.setErrorEnabled(true);
            til_addMaximum.setError("This field is required.");
        }

        if(!minimum.isEmpty()){
            if(Integer.parseInt(minimum) >= 1000){
                valid = true;
            }
        }




        return valid;
    }

    private void volleyPostToServer() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String url = StringConfig.BASE_URL + StringConfig.ROUTE_CREATE_PETITION;

        Map<String, String> mapPostToServer = new HashMap<String, String>();
        mapPostToServer.put("user_id", "1");
        mapPostToServer.put("subject", tet_addSubject.getText().toString());
        mapPostToServer.put("level", spinner_level.getSelectedItem().toString());
        mapPostToServer.put("location", tet_addLocation.getText().toString());
        mapPostToServer.put("description", tet_addDescription.getText().toString());
        mapPostToServer.put("enrolled_students", "0");
        mapPostToServer.put("minimum_price", tet_addMinimum.getText().toString());
        mapPostToServer.put("maximum_price", tet_addMaximum.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(mapPostToServer), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String result = response.getString("result");
                    int petitionId = response.getInt("petition_id");
                    Log.d(StringConfig.LOG_TAG, "result : " + result);
                    Log.d(StringConfig.LOG_TAG, "petitioniD : " + petitionId);

                    clearTextViews();

                    updateEnrollment(petitionId);

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d(StringConfig.LOG_TAG, "responsereponse : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(AddCoursesActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void clearTextViews() {
        tet_addSubject.setText("");
        tet_addLocation.setText("");
        tet_addDescription.setText("");
    }


    private void updateEnrollment(int petitionId) {

        String url = StringConfig.BASE_URL + StringConfig.ROUTE_UPDATE_ENROLLMENT;

        Map<String, String> mapUpdateEnrollment = new HashMap<String, String>();
        mapUpdateEnrollment.put("user_id", "1");
        mapUpdateEnrollment.put("petition_id", String.valueOf(petitionId));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(mapUpdateEnrollment), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");
                    Log.d(StringConfig.LOG_TAG, "updateEnrollment result :  " + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCoursesActivity.this, "An error has occurred.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
