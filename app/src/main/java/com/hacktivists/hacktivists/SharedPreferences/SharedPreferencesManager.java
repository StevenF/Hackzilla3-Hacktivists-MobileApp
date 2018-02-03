package com.hacktivists.hacktivists.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JMM on 2/3/2018.
 */

public class SharedPreferencesManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context){

        sharedPreferences = context.getSharedPreferences("USER_PROFILE", 0);
        editor = sharedPreferences.edit();
    }


    public void setUser_id(String user_id){
        editor.putString("user_id", user_id);
        editor.commit();
    }

    public String getUser_id(){
        return sharedPreferences.getString("user_id", null);
    }

    public void setPetitionTitle(int id, String title){
        editor.putString(String.valueOf(id), title);
        editor.commit();

    }

    public String getPetitionTitle(int id){
        return sharedPreferences.getString(String.valueOf(id), null);
    }

    public void setEmail(String email){
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString("email", null);
    }


}
