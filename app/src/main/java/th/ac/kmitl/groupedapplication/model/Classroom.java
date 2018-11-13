package th.ac.kmitl.groupedapplication.model;

public class Classroom {
    private String[] worked;
    private String class_day;
    private String class_id;
    private String class_start;
    private String class_subject;
    private String prof_id;

    public Classroom(String class_id, String class_subject) {
        this.class_id = class_id;
        this.class_subject = class_subject;
    }

    public Classroom(String class_id, String class_subject, String prof_id) {
        this.class_id = class_id;
        this.class_subject = class_subject;
        this.prof_id = prof_id;
    }

    public Classroom(String[] worked, String class_day, String class_id, String class_start, String class_subject, String prof_id) {
        this.worked = worked;
        this.class_day = class_day;
        this.class_id = class_id;
        this.class_start = class_start;
        this.class_subject = class_subject;
        this.prof_id = prof_id;
    }

    public String[] getWorked() {
        return worked;
    }

    public void setWorked(String[] worked) {
        this.worked = worked;
    }

    public String getClass_day() {
        return class_day;
    }

    public void setClass_day(String class_day) {
        this.class_day = class_day;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_start() {
        return class_start;
    }

    public void setClass_start(String class_start) {
        this.class_start = class_start;
    }

    public String getClass_subject() {
        return class_subject;
    }

    public void setClass_subject(String class_subject) {
        this.class_subject = class_subject;
    }

    public String getProf_id() {
        return prof_id;
    }

    public void setProf_id(String prof_id) {
        this.prof_id = prof_id;
    }
}


