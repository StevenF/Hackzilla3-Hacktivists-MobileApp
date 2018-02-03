package com.hacktivists.hacktivists;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    TextInputEditText tet_loginEmail, tet_loginPassword;
    Button btn_login;

    RequestQueue requestQueue;

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tet_loginEmail = findViewById(R.id.tet_loginEmail);
        tet_loginPassword = findViewById(R.id.tet_loginPassword);

        btn_login = findViewById(R.id.btn_login);

        requestQueue = Volley.newRequestQueue(this);

        sharedPreferencesManager = new SharedPreferencesManager(LoginActivity.this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = tet_loginEmail.getText().toString();
                String password = tet_loginPassword.getText().toString();

                volleyLogin(email, password);

            }
        });

    }

    private void volleyLogin(final String email, String password) {
        String url = StringConfig.BASE_URL + StringConfig.ROUTE_USER_LOGIN;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        Map<String, String> mapLogin = new HashMap<String, String>();
        mapLogin.put("email", email);
        mapLogin.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(mapLogin), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");
                    String user_id = response.getString("user_id");

                    sharedPreferencesManager.setUser_id(user_id);
                    //sharedPreferencesManager.setEmail(email);

                    Log.d(StringConfig.LOG_TAG, "loginResult : " + result);

                    progressDialog.dismiss();

                    if (result.equals("success")) {
                        startActivity(new Intent(LoginActivity.this, NavigationDrawerActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "An error has occurred while logging in.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
