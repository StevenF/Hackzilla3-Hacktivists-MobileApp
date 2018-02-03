package com.hacktivists.hacktivists.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JMM on 2/3/2018.
 */

public class SharedPreferencesStatus {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesStatus(Context context){

        sharedPreferences = context.getSharedPreferences("STATUS", 0);
        editor = sharedPreferences.edit();
    }

    public void setStatus(String id, String status){
        editor.putString(id, status);
        editor.commit();
    }
    public String getStatus(String id){
        return sharedPreferences.getString(id, null);
    }
}
