package th.ac.kmitl.groupedapplication.model;

public class Group {
    private String groupId; // ไอดีจริงๆตาม group list
    private String groupNum; // กลุ่ม 1 2 3 4 5
    private String groupCount; // นับจำนวนคนใน member list
    private String groupME; //check uid กับ member_list แต่ละตัวว่าตรงกับเราไหม
    private String groupProjID; //รับค่า projID ถ้ามี
    private String groupProjName; //รับค่า projName ถ้ามี

    public Group(String groupId, String groupCount, String groupME) {
        this.groupId = groupId;
        this.groupCount = groupCount;
        this.groupME = groupME;
    }

    public Group(String groupId, String groupCount, String groupME,String groupNum, String groupProjName, String groupProjID) {
        this.groupId = groupId;
        this.groupCount = groupCount;
        this.groupME = groupME;
        this.groupNum = groupNum;
        this.groupProjID = groupProjID;
        this.groupProjName = groupProjName;
    }

    public Group(String groupProjID) {
        this.groupProjID = groupProjID;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getGroupME() {
        return groupME;
    }

    public void setGroupME(String groupME) {
        this.groupME = groupME;
    }

    public String getGroupProjID() {
        return groupProjID;
    }

    public void setGroupProjID(String groupProjID) {
        this.groupProjID = groupProjID;
    }

    public String getGroupProjName() {
        return groupProjName;
    }

    public void setGroupProjName(String groupProjName) {
        this.groupProjName = groupProjName;
    }
}
