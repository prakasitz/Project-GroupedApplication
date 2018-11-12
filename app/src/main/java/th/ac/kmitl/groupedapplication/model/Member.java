package th.ac.kmitl.groupedapplication.model;

public class Member {
    private String[] member_list;
    private String member_create;
    private String member_id;
    private String member_post;
    private String member_name;

    public Member(String member_id, String member_name, String member_post) {
        this.member_id = member_id;
        this.member_post = member_post;
        this.member_name = member_name;
    }

    public Member(String member_create, String member_id, String member_name, String member_post) {
        this.member_create = member_create;
        this.member_id = member_id;
        this.member_name = member_name;
        this.member_post = member_post;
    }

    public String[] getMember_list() {
        return member_list;
    }

    public void setMember_list(String[] member_list) {
        this.member_list = member_list;
    }

    public String getMember_create() {
        return member_create;
    }

    public void setMember_create(String member_create) {
        this.member_create = member_create;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_post) {
        this.member_name = member_name;
    }

    public String getMember_post() {
        return member_post;
    }

    public void setMember_post(String member_post) {
        this.member_post = member_post;
    }
}
