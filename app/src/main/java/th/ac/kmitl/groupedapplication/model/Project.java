package th.ac.kmitl.groupedapplication.model;

public class Project {
    private String[] project_list;
    private String project_create;
    private String project_id;
    private String project_name;

    public Project(String project_id, String project_name) {
        this.project_id = project_id;
        this.project_name = project_name;
    }

    public Project(String project_create, String project_id, String project_name) {
        this.project_create = project_create;
        this.project_id = project_id;
        this.project_name = project_name;
    }

    public String[] getProject_list() {
        return project_list;
    }

    public void setProject_list(String[] project_list) {
        this.project_list = project_list;
    }

    public String getProject_create() {
        return project_create;
    }

    public void setProject_create(String project_create) {
        this.project_create = project_create;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
