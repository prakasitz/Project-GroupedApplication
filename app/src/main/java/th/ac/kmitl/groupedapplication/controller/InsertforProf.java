package th.ac.kmitl.groupedapplication.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

import th.ac.kmitl.groupedapplication.ClassroomActivity;
import th.ac.kmitl.groupedapplication.ClassroomCreateActivity;

public class InsertforProf {
    private final String key;
    private final String uid;
    private String classid;
    private String classname;
    private Activity actCur;

    private final DatabaseReference reference;
    private final Map<String, Object> table;

    private final Context contextCur;
    private final Class<?> acttarget;

    public InsertforProf(String uID, String k, DatabaseReference ref, Map<String, Object> tb,
                         Context contxtcur, Class<?> acttarget, Activity actcur) {
        this.key = k;
        this.reference = ref;
        this.table = tb;
        this.contextCur = contxtcur;
        this.uid = uID;
        this.actCur = actcur;
        this.acttarget = acttarget;
        setInsert();
    }

    public InsertforProf(String uID, String k, String classid, String classname, DatabaseReference ref, Map<String, Object> tb,
                         Context contxtcur, Class<?> acttarget, Activity actcur) {
        this.key = k;
        this.reference = ref;
        this.table = tb;
        this.contextCur = contxtcur;
        this.uid = uID;
        this.classid = classid;
        this.classname = classname;
        this.actCur = actcur;
        this.acttarget = acttarget;
        setInsert();
    }

    public void setInsert() {
        reference.child(key).updateChildren(table, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(contextCur ,"บันทึกข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(actCur.getApplicationContext(), acttarget);

                    if(classid == null) { //สำหรบหน้า createClass
                        i.putExtra("uid",uid);
                        i.putExtra("ustatus","1");
                        actCur.finish();
                        actCur.startActivity(i);
                    } else {
                        actCur.finish();
                    }


                } else {
                    Toast.makeText(contextCur ,"ไม่สามารถบันทึกข้อมูลได้ โปรดลองเชื่อมต่อใหม่",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
