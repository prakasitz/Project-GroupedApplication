package th.ac.kmitl.groupedapplication.controller;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class setShowCount {

    private final Button btn;
    private final String btnText;
    private final String classID;

    private String btnNewText;
    private String indexOfprojList;
    private String indexOfgroupList;
    public static boolean hasgroup;

    public setShowCount(Button btnView, String classid, String p_uid) {
        this.classID = classid;
        String profressorID = p_uid;
        if(!profressorID.equals("")) {
            this.indexOfprojList = classid + "_" + profressorID;
        }

        this.btn = btnView;
        this.btnText = btn.getText().toString();
        countList(btn);
    }

    public setShowCount(Button btnView, String classid) {
        this.classID = classid;
        this.indexOfgroupList = classid;
        this.btn = btnView;
        this.btnText = btn.getText().toString();
        countList(btn);
    }

    private void countList(final Button btn) {
        String btnDesc = btn.getContentDescription().toString();
        Log.e("btnDesc",btnDesc);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        switch (btnDesc) {
            case "member_list":
                myRef = database.getReference("classrooms/" + classID);
                myRef.child(btnDesc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String count = String.valueOf(dataSnapshot.getChildrenCount());
                        Log.w("countMember", count);
                        setNewTextButton(count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("error", databaseError.toException());
                    }
                });
                break;
            case "project_list":
                myRef = database.getReference(btnDesc);
                myRef.orderByChild("class_uid").equalTo(indexOfprojList).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String count = String.valueOf(dataSnapshot.getChildrenCount());
                        Log.w("countProjectList", count);
                        if (count.equals("0")) {
                            btnNewText = btnText + " (ว่าง)";
                        } else {
                            btnNewText = btnText + " (" + count + ")";
                        }
                        btn.setText(btnNewText);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("error", databaseError.toException());
                    }
                });
                break;
            case "group_list":
                myRef = database.getReference(btnDesc);
                myRef.orderByChild("class_id").equalTo(indexOfgroupList).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String count = String.valueOf(dataSnapshot.getChildrenCount());
                        Log.w("countProjectList", count);
                        if (count.equals("0")) {
                            btnNewText = btnText + " (ว่าง)";
                            hasgroup = false;
                        } else {
                            btnNewText = btnText + " (" + count + ")";
                            hasgroup = true;
                        }
                        btn.setText(btnNewText);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("error", databaseError.toException());
                    }
                });
                break;
            default:
                Log.e("don't move", "ok");
                break;
        }



    }

    private void setNewTextButton(String cnt) {
        if(cnt.equals("0")) {
            btnNewText = btnText+" (ว่าง)";
        } else {
            btnNewText = btnText + " ("+cnt+")";
        }
        btn.setText(btnNewText);
    }
}
