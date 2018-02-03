package com.hacktivists.hacktivists.GetterSetter;

/**
 * Created by steven on 2/2/18.
 */

public class RParserPetitionList {
    private int id;
    private String subject;
    private String level;
    private String location;
    private int enrolled_students;
    private String description;
    private int user_id;


    public RParserPetitionList(int id, String subject, String level, String location, int enrolled_students, String description, int user_id) {
        this.id = id;
        this.subject = subject;
        this.level = level;
        this.location = location;
        this.enrolled_students = enrolled_students;
        this.description = description;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getLevel() {
        return level;
    }

    public String getLocation() {
        return location;
    }

    public int getEnrolled_students() {
        return enrolled_students;
    }

    public String getDescription() {
        return description;
    }

    public int getUser_id() {
        return user_id;
    }
}
