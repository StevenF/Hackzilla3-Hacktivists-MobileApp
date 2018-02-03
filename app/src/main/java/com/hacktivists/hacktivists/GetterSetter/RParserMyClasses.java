package com.hacktivists.hacktivists.GetterSetter;

/**
 * Created by JMM on 2/3/2018.
 */

public class RParserMyClasses {

    private int id;
    private int user_id;
    private int petition_id;
    private String date;
    private String tutorname;
    private String status;

    public RParserMyClasses(int id, int user_id, int petition_id, String date, String tutorname, String status){

        this.id = id;
        this.user_id = user_id;
        this.petition_id = petition_id;
        this.date = date;
        this.tutorname = tutorname;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getPetition_id() {
        return petition_id;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getTutorname() {
        return tutorname;
    }

    public String getStatus() {
        return status;
    }
}
