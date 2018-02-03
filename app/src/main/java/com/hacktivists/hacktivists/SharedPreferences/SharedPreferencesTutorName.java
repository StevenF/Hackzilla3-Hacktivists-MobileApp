package com.hacktivists.hacktivists.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JMM on 2/3/2018.
 */

public class SharedPreferencesTutorName {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesTutorName(Context context){

        sharedPreferences = context.getSharedPreferences("TUTOR_NAME", 0);
        editor = sharedPreferences.edit();
    }

    public void setTutorName(String id, String tutorName){
        editor.putString(id, tutorName);
        editor.commit();
    }

    public String getTutorName(String id){
        return sharedPreferences.getString(id, null);
    }
}
