package com.hacktivists.hacktivists;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hacktivists.hacktivists.Config.StringConfig;
import com.hacktivists.hacktivists.GetterSetter.RParserMyClasses;
import com.hacktivists.hacktivists.RecyclerAdapter.RAdapterMyClasses;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesManager;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesStatus;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesTutorName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyClasses extends Fragment {

    RecyclerView rv_myClasses;
    RAdapterMyClasses rAdapterMyClasses;
    List<RParserMyClasses> rParserMyClassesList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;

    RequestQueue requestQueue;

    SharedPreferencesManager sharedPreferencesManager;
    SharedPreferencesTutorName sharedPreferencesTutorName;
    SharedPreferencesStatus sharedPreferencesStatus;



    public FragmentMyClasses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_classes, container, false);

        rv_myClasses = view.findViewById(R.id.rv_myClasses);

        requestQueue = Volley.newRequestQueue(getContext());

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        sharedPreferencesTutorName = new SharedPreferencesTutorName(getContext());
        sharedPreferencesStatus = new SharedPreferencesStatus(getContext());

        initRecyclerViewAdapter();

        return view;
    }

    private void initRecyclerViewAdapter() {
        rParserMyClassesList.clear();

        rAdapterMyClasses = new RAdapterMyClasses(getContext(), rParserMyClassesList, new RAdapterMyClasses.RAdapterMyClassesOnItemClick() {
            @Override
            public void onItemClick(int user_id, int petition_id) {
                volleyCancel(user_id, petition_id);
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_myClasses.setLayoutManager(linearLayoutManager);
        rv_myClasses.setAdapter(rAdapterMyClasses);

        volleyMyClasses();

    }


    private void volleyMyClasses(){

        String url = StringConfig.BASE_URL + StringConfig.ROUTE_MY_ENROLLED;

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getContext());
        String user_id = sharedPreferencesManager.getUser_id();

        Map<String, String> mapMyClasses = new HashMap<String, String>();
        mapMyClasses.put("user_id", user_id);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, new JSONObject(mapMyClasses), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i=0;i<=response.length();i++){
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);

                        Log.d(StringConfig.LOG_TAG, "jsonObjecttttt :  " + jsonObject);
                        int id = jsonObject.getInt("id");
                        int user_id = jsonObject.getInt("user_id");
                        int petition_id = jsonObject.getInt("petition_id");
                        String created_at = jsonObject.getString("created_at");

                        Log.d(StringConfig.LOG_TAG, "created_at : " + created_at);

                        String tutor_name = sharedPreferencesTutorName.getTutorName(String.valueOf(petition_id));
                        String status = sharedPreferencesStatus.getStatus(String.valueOf(petition_id));


                        RParserMyClasses rParserMyClasses = new RParserMyClasses(id, user_id, petition_id, created_at, tutor_name, status);
                        rParserMyClassesList.add(rParserMyClasses);;
                        rAdapterMyClasses.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void volleyCancel(int id, final int petition_id){

        String url = StringConfig.BASE_URL + StringConfig.ROUTE_CANCEL;

        Map<String,String> mapCancel = new HashMap<String, String>();
        mapCancel.put("id", String.valueOf(id));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(mapCancel), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(StringConfig.LOG_TAG, "cancelcancel : " + response);

                    String result = response.getString("result");

                    Log.d(StringConfig.LOG_TAG, "result :  " + result);
                    if(result.equals("success")){
                        initRecyclerViewAdapter();
                        Toast.makeText(getContext(), sharedPreferencesManager.getPetitionTitle(petition_id) +  " has been removed from your classes.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error removing the subject", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
