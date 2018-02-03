package com.hacktivists.hacktivists;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hacktivists.hacktivists.Config.StringConfig;
import com.hacktivists.hacktivists.GetterSetter.RParserPetitionList;
import com.hacktivists.hacktivists.RecyclerAdapter.RAdapterPetitionList;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesManager;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesStatus;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesTutorName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBrowseClasses extends Fragment {


    RequestQueue requestQueue;

    RecyclerView rv_petitionList;
    RParserPetitionList rParserPetitionList;
    List<RParserPetitionList> rParserPetitionListList = new ArrayList<>();
    RAdapterPetitionList rAdapterPetitionList;

    LinearLayoutManager linearLayoutManager;

    SwipeRefreshLayout srl_browseClasses;

    Button btn_petitionStart;

    SharedPreferencesManager sharedPreferencesManager;
    SharedPreferencesTutorName sharedPreferencesTutorName;
    SharedPreferencesStatus sharedPreferencesStatus;

    public FragmentBrowseClasses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse_classes, container, false);

        requestQueue = Volley.newRequestQueue(getContext());
        rv_petitionList = view.findViewById(R.id.rv_petitionList);
        rAdapterPetitionList = new RAdapterPetitionList(getContext(), rParserPetitionListList);

        srl_browseClasses = view.findViewById(R.id.srl_browseClasses);

        btn_petitionStart = view.findViewById(R.id.btn_petitionStart);

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        sharedPreferencesTutorName = new SharedPreferencesTutorName(getContext());
        sharedPreferencesStatus = new SharedPreferencesStatus(getContext());

        initReyclerView();

        srl_browseClasses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initReyclerView();
            }
        });

        btn_petitionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCoursesActivity.class));
            }
        });

        return view;

    }

    private void initReyclerView(){

        rParserPetitionListList.clear();

        linearLayoutManager  = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_petitionList.setLayoutManager(linearLayoutManager);
        rv_petitionList.setAdapter(rAdapterPetitionList);

        fetchPetitionClasses();

    }

    private void fetchPetitionClasses(){

        srl_browseClasses.setRefreshing(true);

        String url = StringConfig.BASE_URL + StringConfig.ROUTES_PETITION_SHOW;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("petitions");

                    Log.d(StringConfig.LOG_TAG, "jsonArray : " + jsonArray);

                    for(int i=0; i <= jsonArray.length(); i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id = jsonObject1.getInt("id");
                        String subject = jsonObject1.getString("subject");
                        String level = jsonObject1.getString("level");
                        String location = jsonObject1.getString("location");
                        int enrolled_students = jsonObject1.getInt("enrolled_students");
                        String description = jsonObject1.getString("description");
                        int user_id = jsonObject1.getInt("user_id");
                        String tutor_fullname = jsonObject1.getString("tutor_fullname");
                        String status = String.valueOf(jsonObject1.getBoolean("is_posted"));

                        sharedPreferencesManager.setPetitionTitle(id, subject);
                        sharedPreferencesTutorName.setTutorName(String.valueOf(id), tutor_fullname);
                        sharedPreferencesStatus.setStatus(String.valueOf(id), status);

                        rParserPetitionList = new RParserPetitionList(id, subject, level, location, enrolled_students, description, user_id);
                        rParserPetitionListList.add(rParserPetitionList);
                        rAdapterPetitionList.notifyDataSetChanged();

                        srl_browseClasses.setRefreshing(false);
                    }


                }catch(JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(StringConfig.LOG_TAG, "volleyError : " +error.toString());


                srl_browseClasses.setRefreshing(false);

                Toast.makeText(getContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(stringRequest);
    }


}
