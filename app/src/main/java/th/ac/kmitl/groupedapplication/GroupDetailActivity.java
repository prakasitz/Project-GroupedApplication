package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import th.ac.kmitl.groupedapplication.adapter.GroupItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.GroupListAdapter;
import th.ac.kmitl.groupedapplication.adapter.MemberItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.MemberListAdapter;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Group;
import th.ac.kmitl.groupedapplication.model.Member;

public class GroupDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , MemberItemClickListener {
    //GroupDetail use .....
    //GroupDetail จากหน้า Group สามารถกดเข้ามานี้ได้ทุก group แต่ถ้ากลุ่มตัวเองจะ visable ปุ่ม เลือกโปรเจ็ค เช็กจาก มี uid ของ user นี่ อยุในกลุ่มไหม
    //หน้าานี้จะโชว วิชา , โปรเจคที่ทำ กลุ่มที่
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference memRef;
    private DatabaseReference userRef;
    private DatabaseReference groupRef;
    private DatabaseReference projRef;
    private ValueEventListener valueEventListener_users_in_member;
    private ValueEventListener valueEventListener_group;
    //-------------View-----------------
    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvSubject;
    private TextView tvProject;
    private TextView tvMemberCount;
    private Button btnChooseProject;
    private RecyclerView recyclerView;
    //------------String-----------------
    private String uid;
    private String ustatus;
    private String classid;
    private String classname;
    private String puid;
    private String class_puid;
    private String groupid;
    private String groupnum;
    private boolean groupme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //--------------mthu-------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //------------setVisibility-----------------------------------------------
        findViewById(R.id.inc_group_detail).setVisibility(View.VISIBLE);
        ///--------------recycleview-----------------------------------------------
        recyclerView = findViewById(R.id.recyclerView_GroupDetail);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        //----------------view----------------------------------------------------
        tvProject = findViewById(R.id.title_head_project);
        tvSubject = findViewById(R.id.title_class_in_group_detail);
        tvMemberCount = findViewById(R.id.title_member_in_group);
        btnChooseProject = findViewById(R.id.btn_select_proj);
        //-------รับค่าจากหน้า MenuStudent ต้องเอา puid มาให้ได้อะ , MenuTeacher ส่งได้ไม่มีปัญหา
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        puid = getI.getStringExtra("puid");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
        class_puid = getI.getStringExtra("class_puid");
        groupid = getI.getStringExtra("groupid");
        groupnum = getI.getStringExtra("groupnum");
        groupme = getI.getBooleanExtra("groupme",false);
        //------------------setText subject---------------------------------------
        tvSubject.setText(classname);
        setTitle("กลุ่มที่ " + groupnum + " (ฉัน)");
        //----------------------setOnclick--------------------------------------
        Log.e("GDetail: groupME? ->", String.valueOf(groupme));
        if(groupme == false) {
            setTitle("กลุ่มที่ " + groupnum);
            btnChooseProject.setVisibility(View.GONE);
        } else {
            btnChooseProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GroupDetailActivity.this, "ไปหน้าเลือกโปรเจ็กต์", Toast.LENGTH_SHORT);
                    Intent i = new Intent(GroupDetailActivity.this, SelectProjectActivity.class);
                    i.putExtra("class_puid", class_puid);
                    i.putExtra("classid", classid);
                    i.putExtra("uid", uid);
                    i.putExtra("ustats", ustatus);
                    i.putExtra("classname",classname);
                    i.putExtra("puid",puid);
                    i.putExtra("groupid",groupid);
                    startActivity(i);
                }
            });
        }

        groupRef = database.getReference("group_list");
        projRef = database.getReference("project_list");
        Query groupQuery = groupRef.child(groupid);
        valueEventListener_group = groupQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("groupQuery",dataSnapshot.toString());
                for (DataSnapshot dataDetail: dataSnapshot.getChildren()) {
                    if(dataDetail.getKey().equals("project_id") && dataDetail.getValue().toString() != null) {
                        String projKey = dataDetail.getValue().toString();
                        Log.e("ProjectKey", dataDetail.getValue().toString());
                        Query projQuery = projRef.child(projKey).child("proj_name");
                        projQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.e("projQuery",dataSnapshot.toString());
                                String projName = dataSnapshot.getValue().toString();
                                Log.e("projName", projName);
                                tvProject.setText("หัวข้อ : "+projName);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { Log.d("errorConnect", String.valueOf(databaseError)); }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { Log.d("errorConnect", String.valueOf(databaseError)); }
        });

        //------------------------coding-----------------------------------------
        //----------------------draw nav bar--------------------------------------
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
        if (ustatus.equals("0")) {
            Menu menubar = navigationView.getMenu();
            menubar.findItem(R.id.nav_classcreate).setVisible(false);
        }
        getDataResults(); //สำหรับทำสมาชิก
    }

    public void getDataResults() {
        final ArrayList<Member> memberArrayList = new ArrayList<Member>();
        database = FirebaseDatabase.getInstance();
        memRef = database.getReference("group_list/" + groupid);
        Query memQuery =  memRef.child("member_list");
        memQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String std_id;
                    tvMemberCount.setText("สมาชิก (" + dataSnapshot.getChildrenCount() + ")");
                    for (DataSnapshot getUIDINmember : dataSnapshot.getChildren()) {
                        std_id = getUIDINmember.getValue().toString();
                        Log.e("std_id", std_id);

                        userRef = database.getReference("users");
                        Query userQuery = userRef.child(std_id);
                        valueEventListener_users_in_member = userQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    String stdID = null, stdfname = null, stdlname  = null, stdpost = null, stdfullname;
                                    for (DataSnapshot getUser : dataSnapshot.getChildren()) {
                                        switch (getUser.getKey()) {
                                            case "id":
                                                stdID = getUser.getValue().toString();
                                                Log.e("std_id", stdID);
                                                break;
                                            case "fname":
                                                stdfname = getUser.getValue().toString();
                                                Log.e("std_fname", stdfname);
                                                break;
                                            case "lname":
                                                stdlname = getUser.getValue().toString();
                                                Log.e("std_lname", stdlname);
                                                break;
                                            case "post":
                                                stdpost = getUser.getValue().toString();
                                                Log.e("std_post", stdpost);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    stdfullname = stdfname + " " + stdlname;
                                    Member member = new Member(stdID, stdfullname, stdpost);
                                    memberArrayList.add(member);
                                    getAllMember(memberArrayList);
                                } catch (Exception e) {
                                    Log.e("memberChild", e.toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }
                } catch (Exception e) {
                    Log.e("DataChangeMemberError", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void getAllMember(ArrayList<Member> Memberlist) {
        ArrayList<Member> members = Memberlist;
        Log.d("MemberInGroupAdapters", String.valueOf(Memberlist.size()));
        MemberListAdapter MemberAdapter = new MemberListAdapter(members, GroupDetailActivity.this);
        Log.d("MemberInGroupAdapter", String.valueOf(MemberAdapter.getItemCount()));
        recyclerView.setAdapter(MemberAdapter);
    }

    @Override
    public void onMemberItemClick(String memberID) {
        Toast.makeText(this, "MemberINgroup: " + memberID, Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, GroupDetailActivity.class);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        i.putExtra("classid", classid);
        i.putExtra("class_puid",class_puid);
        i.putExtra("groupid",groupId);
        startActivity(i);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListener_users_in_member != null || valueEventListener_group != null) {
            userRef.removeEventListener(valueEventListener_users_in_member);
            groupRef.removeEventListener(valueEventListener_group);
        }
    }


    //-------------template----------------------
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
        //หน้านี้ม่อยากให้มี memu bar
        //---------nav head-----------------
        tvEmail = findViewById(R.id.textEmail);
        tvFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid, tvEmail, tvFullName);
        Log.e("CreateOpMenu", "ok");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(GroupDetailActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_group_detail).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(GroupDetailActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_group_detail).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(GroupDetailActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(GroupDetailActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_group_detail).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
