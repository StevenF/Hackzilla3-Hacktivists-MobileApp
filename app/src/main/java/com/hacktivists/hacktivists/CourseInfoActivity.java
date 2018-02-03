package com.hacktivists.hacktivists;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hacktivists.hacktivists.Config.StringConfig;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CourseInfoActivity extends AppCompatActivity {

    Bundle bundle;
    TextView tv_course_info_subject, tv_course_info_description, tv_course_info_numberOfEnrolled, tv_course_info_plurality;
    Button btn_course_info_enroll;

    String subject, petition_id;

    RequestQueue requestQueue;

    SharedPreferencesManager sharedPreferencesManager;

    LinearLayout ll_courseInfoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        tv_course_info_subject = findViewById(R.id.tv_course_info_subject);
        tv_course_info_description = findViewById(R.id.tv_course_info_description);
        tv_course_info_numberOfEnrolled = findViewById(R.id.tv_course_info_numberOfEnrolled);
        tv_course_info_plurality = findViewById(R.id.tv_course_info_plurality);

        btn_course_info_enroll = findViewById(R.id.btn_course_info_enroll);

        requestQueue = Volley.newRequestQueue(this);

        sharedPreferencesManager = new SharedPreferencesManager(CourseInfoActivity.this);

        ll_courseInfoActivity = findViewById(R.id.ll_courseInfoActivity);

        bundle = getIntent().getExtras();

        if(bundle!=null){

            subject = bundle.getString("subject");
            petition_id = bundle.getString("petition_id");
            String level = bundle.getString("level");
            String location  = bundle.getString("location");
            String enrolled_students = bundle.getString("enrolled_students");
            String description = bundle.getString("description");
            String user_id = bundle.getString("user_id");


            tv_course_info_subject.setText(subject);
            tv_course_info_description.setText(description);
            tv_course_info_numberOfEnrolled.setText(enrolled_students);

            if(Integer.parseInt(enrolled_students) <= 1){
                tv_course_info_plurality.setText(" student");
            }else {
                tv_course_info_plurality.setText(" students");
            }

        }


        btn_course_info_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseInfoActivity.this);
                builder.setMessage("Are you sure you want to enroll in this subject ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                volleyEnroll();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void volleyEnroll(){

        String url = StringConfig.BASE_URL + StringConfig.ROUTE_ENROLL;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enrolling to "+subject+"..");
        progressDialog.show();

        Map<String, String> mapLogin = new HashMap<String, String>();
        mapLogin.put("user_id", sharedPreferencesManager.getUser_id());
        mapLogin.put("petition_id", petition_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(mapLogin), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");

                    Log.d(StringConfig.LOG_TAG, "enrollResult : " + result);

                    progressDialog.dismiss();

                    if (result.equals("success")) {
                        Snackbar snackbar = Snackbar.make(ll_courseInfoActivity, "You are now enrolled to " + subject, Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        //Toast.makeText(CourseInfoActivity.this, "You are now enrolled to " + subject, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CourseInfoActivity.this, "An error has occurred while enrolling.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}
