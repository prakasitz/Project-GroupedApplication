package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import th.ac.kmitl.groupedapplication.controller.setNavHeader;

public class SelectProjectActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> projName = new ArrayList<String>();
    private ArrayList<String> ProjKey = new ArrayList<String>();
    private int indexProj;
    private String class_puid, uid, ustatus, classname, puid, groupid, classid;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference projRef;
    private DatabaseReference groupRef;
    private DatabaseReference chkProjRef;
    private ValueEventListener valueEventListener_proj;

    private TextView tvSubject;
    private Spinner spinner_d;
    private Button btn_accept;
    private TextView tvEmail;
    private TextView tvFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //-----------firebase set----------------------
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //-----------get Intent------------------------
        Intent getI = getIntent();
        class_puid = getI.getStringExtra("class_puid");
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustats");
        classname = getI.getStringExtra("classname");
        puid = getI.getStringExtra("puid");
        groupid = getI.getStringExtra("groupid");
        classid = getI.getStringExtra("classid");
        //------------setVisibility---------------
        findViewById(R.id.inc_select_proj).setVisibility(View.VISIBLE);
        //------------View-----------------------------
        spinner_d = findViewById(R.id.spinner_dropdown);
        btn_accept = findViewById(R.id.btn_accept);
        tvSubject = findViewById(R.id.title_class_in_select_proj);
        btn_accept.setOnClickListener(this);

        tvSubject.setText(classname);

        createProjName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, projName);
        spinner_d.setAdapter(adapter);


        spinner_d.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                Log.wtf("projKey",String.valueOf(ProjKey));
                indexProj = position;
                chkProjRef = database.getReference("group_list");
                if(indexProj != 0 && ProjKey.size() != 0 ) {
                    chkProjRef.orderByChild("class_id").equalTo(classid).addListenerForSingleValueEvent(new ValueEventListener() { //เช็กกลุ่มทั้งหมดในคลาสนี้
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e("dataSnapshot",dataSnapshot.toString());
                            for(DataSnapshot inGroup : dataSnapshot.getChildren()) { //วนหาเลยจ้า
                                Log.e("inGroup",inGroup.toString());
                                Log.e("inGroupClass_Id",inGroup.child("class_id").toString());
                                if(inGroup.hasChild("project_id")) {
                                    String projectKey = inGroup.child("project_id").getValue().toString();
                                    Log.e("size",ProjKey.size()+"_"+indexProj);
                                    if(indexProj != 0 && ProjKey.get(indexProj-1).equals(projectKey) ) {
                                        Log.e("NoSelect","this ซ้ำ");
                                        Toast.makeText(SelectProjectActivity.this,
                                                "\""+projName.get(position) + "\" , ซ้ำ! กรุณาเลือกใหม่",
                                                Toast.LENGTH_LONG).show();
                                        spinner_d.setSelection(0);
                                        break;
                                    } else {
                                        Log.e("YesSelect","this ได้");
                                        Toast.makeText(SelectProjectActivity.this,
                                                "คุณเลือก \"" + projName.get(position) +"\"",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //----------------set layout-----------------------
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //-------------create nav------------
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
        if (ustatus.equals("0")) {
            Menu menubar = navigationView.getMenu();
            menubar.findItem(R.id.nav_classcreate).setVisible(false);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListener_proj != null) {
            projRef.removeEventListener(valueEventListener_proj);
        }
    }

    public void  createProjName(){
        projName.add("---กรุณาเลือก หัวข้อโปรเจ็กต์---");
        projRef = database.getReference("project_list");
        valueEventListener_proj = projRef.orderByChild("class_uid").equalTo(class_puid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot projData = dataSnapshot;
                Log.wtf("projData", String.valueOf(projData));
                for(DataSnapshot projAcc : projData.getChildren()){
                    Log.wtf("projInArray",String.valueOf(projAcc.child("proj_name").getValue()));
                    Log.wtf("projInArray",String.valueOf(projAcc.getKey()));
                    ProjKey.add(String.valueOf(String.valueOf(projAcc.getKey())));
                    projName.add(String.valueOf(projAcc.child("proj_name").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errorConnect", String.valueOf(databaseError));
            }
        });
    }
    public void onClick(View v) {
        if (v.getId() == R.id.btn_accept) {
            groupRef = database.getReference("group_list");
            groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> groupTable = new HashMap<>();
                    DataSnapshot groupList = dataSnapshot;
                    Log.wtf("groupList",String.valueOf(groupList));
                    if( indexProj-1 >= 0) {
                        groupTable.put(groupid+"/project_id", String.valueOf(ProjKey.get(indexProj - 1)));
                        groupRef.updateChildren(groupTable);
                        Log.wtf("projKey", String.valueOf(indexProj - 1));
                        Log.wtf("projKey", String.valueOf(ProjKey.get(indexProj - 1)));
                        Toast.makeText(SelectProjectActivity.this, "เลือกหัวข้อเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SelectProjectActivity.this, "กรุณาเลือกหัวข้อโปรเจ็กต์", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("errorConnect", String.valueOf(databaseError));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //---------nav head-----------------
        tvEmail = findViewById(R.id.textEmail);
        tvFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid,tvEmail,tvFullName);
        Log.e("CreateOpMenu","ok");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(SelectProjectActivity.this, ProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_select_proj).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            Intent i = new Intent(SelectProjectActivity.this, ClassroomActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("uid", uid);
            i.putExtra("ustatus", setNavHeader.ustatus);
            startActivity(i);
            findViewById(R.id.inc_select_proj).setVisibility(View.GONE);
            finish();
            //แค่ GONE หายไป
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(SelectProjectActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(SelectProjectActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_select_proj).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
