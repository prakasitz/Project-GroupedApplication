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
import java.util.HashMap;
import java.util.Map;

import th.ac.kmitl.groupedapplication.adapter.GroupItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.GroupListAdapter;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Group;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GroupItemClickListener {
    //Group use addChildEventListeneru
    //Group show in student(current) and stu
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference groupRef;
    private DatabaseReference projRef;
    private ValueEventListener valueEventListener_project;
    private ChildEventListener childEventListener_group;
    //-------------View-----------------
    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvSubject;
    private RecyclerView recyclerView;
    //------------String-----------------
    private String uid;
    private String ustatus;
    private String classid;
    private String classname;
    private String puid;
    private String class_puid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //--------------mthu-------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        ///--------------recycleview-----------------------------------------------
        recyclerView = findViewById(R.id.recyclerView_Group);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ////-------------------coding---------------------------------------------
        //------------setVisibility-----------------------------------------------
        findViewById(R.id.inc_group_list).setVisibility(View.VISIBLE);
        //----------------view----------------------------------------------------
        tvSubject = findViewById(R.id.title_class_in_group_list);
        //-------รับค่าจากหน้า MenuStudent ต้องเอา puid มาให้ได้อะ , MenuTeacher ส่งได้ไม่มีปัญหา
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        puid = getI.getStringExtra("puid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
        class_puid = classid + "_" + puid;
        //------------------setText subject---------------------------------------
        tvSubject.setText(classname);
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
        getDataResults();
    }


    private String groupID;
    private boolean groupMe;
    private String groupCount;
    private String projKey = "";
    private String projName;
    private Query GroupQuery;
    private Query ProjQuery;
    private Map<String, String> map = new HashMap<>();

    public void getDataResults() {
        final ArrayList<Group> groupArrayList = new ArrayList<Group>();
        database = FirebaseDatabase.getInstance();
        groupRef = database.getReference("group_list");
        projRef = database.getReference("project_list");
        GroupQuery = groupRef.orderByChild("class_id").equalTo(classid);
        ProjQuery = projRef.orderByChild("class_uid").equalTo(class_puid); //จำนวนโปรเจคทีไ่ด้มาต้องน้อยกว่า โปรเจคทั้งหมดแน่นอน
        valueEventListener_project = ProjQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (groupArrayList.size() > 0) {
                        groupArrayList.clear();
                    }
                    //คิวรีที่ได้คือ จะได้แค่ โปรเจคของคลาสที่อาจารย์คนนั้นสอนเท่านั้น
                    Log.e("projQuery", dataSnapshot.toString());

                    for (DataSnapshot inChild : dataSnapshot.getChildren()) {
                        Log.e("projKey + projName", inChild.getKey().toString() + " and " +
                                inChild.child("proj_name").getValue().toString()); // ได้ projkey + projName
                        String projKey = inChild.getKey();
                        String projName = inChild.child("proj_name").getValue().toString();
                        map.put(projKey, projName); //เราจะได้ key และ value ตามจำนวนในดาต้า
                    }


                    childEventListener_group = GroupQuery.addChildEventListener(new ChildEventListener() {
                        int groupNum = 0;
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            try {
                                Log.e("key_group", dataSnapshot.getKey());
                                groupID = dataSnapshot.getKey();
                                groupMe = false;
                                groupNum++;
                                projName = null;
                                groupCount = String.valueOf(dataSnapshot.child("member_list").getChildrenCount());
                                for (DataSnapshot findME : dataSnapshot.child("member_list").getChildren()) {
                                    if (findME.getValue().toString().equals(uid)) {
                                        groupMe = true;
                                        Log.e("groupMe", String.valueOf(groupMe));
                                        break;
                                    }
                                }

                                Log.w("datahasProject_id", String.valueOf(dataSnapshot.hasChild("project_id")));
                                if (dataSnapshot.hasChild("project_id")) {
                                    projKey = dataSnapshot.child("project_id").getValue().toString();
                                    Log.e("projkey", projKey);
                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        Log.e("entryMap", "key: " + entry.getKey() + " value: " + entry.getValue()); //วิธี getKey และ value
                                        if (projKey.equals(entry.getKey())) {
                                            projName = entry.getValue();
                                            Log.e("projMap", "key: " + projKey + " value: " + projName);
                                            break;
                                        }
                                    }
                                } else {
                                    Log.w("dataSnap", String.valueOf(dataSnapshot.toString()));
                                }
                                Log.e("groupMEEE", "+" + groupMe);
                                Group groupFull = new Group(groupID, groupCount, groupMe, String.valueOf(groupNum), projName);
                                groupArrayList.add(groupNum-1, groupFull);
                                if(groupArrayList.size() != 0) {
                                    getAllGroup(groupArrayList);
                                } else {
                                    Toast.makeText(GroupActivity.this, "ห้องเรียนยังไม่มีการจัดกลุ่ม กรุณารอครูจัดกลุ่มก่อน",Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e("groupActivity",e.toString());
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            int ind = 0;
                            Log.e("key_group_change", dataSnapshot.getKey());
                            groupID = dataSnapshot.getKey();
                            groupMe = false;
                            projName = null;
                            groupCount = String.valueOf(dataSnapshot.child("member_list").getChildrenCount());
                            Log.e("change_groupArraySize", String.valueOf(groupArrayList.size()));
                            for(int i = 0 ; i < groupArrayList.size() ; i++) {
                                String groupid = groupArrayList.get(i).getGroupId();
                                if(groupID.equals(groupid)) {
                                    ind = i;
                                    groupNum = i+1;
                                    Log.e("SSSSS",groupid + "__"+ groupID+"_ind_"+ind);
                                    break;
                                }
                            }

                            groupArrayList.remove(ind);

                            for (DataSnapshot findME : dataSnapshot.child("member_list").getChildren()) {
                                if (findME.getValue().toString().equals(uid)) {
                                    groupMe = true;
                                    Log.e("groupMe", String.valueOf(groupMe));
                                    break;
                                }
                            }

                            Log.w("datahasProject_id", String.valueOf(dataSnapshot.hasChild("project_id")));
                            if (dataSnapshot.hasChild("project_id")) {
                                projKey = dataSnapshot.child("project_id").getValue().toString();
                                Log.e("projkey", projKey);
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    Log.e("entryMap", "key: " + entry.getKey() + " value: " + entry.getValue()); //วิธี getKey และ value
                                    if (projKey.equals(entry.getKey())) {
                                        projName = entry.getValue();
                                        Log.e("projMap", "key: " + projKey + " value: " + projName);
                                        break;
                                    }
                                }
                            } else {
                                Log.w("dataSnap", String.valueOf(dataSnapshot.toString()));
                            }
                            Log.e("groupMEEE", "+" + groupMe);
                            Group groupFull = new Group(groupID, groupCount, groupMe, String.valueOf(groupNum), projName);
                            groupArrayList.add(ind, groupFull);
                            getAllGroup(groupArrayList);
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            Toast.makeText(GroupActivity.this, "กลุ่มทั้งหมดถูกยุบแล้ว กรุณารออาจารย์เลือกกลุ่มอีกครั้ง", Toast.LENGTH_LONG).show();
                            groupArrayList.clear();
                            getAllGroup(groupArrayList);
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("errorConnectDataChange", String.valueOf(databaseError));
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(GroupActivity.this, "Error! DataChange", Toast.LENGTH_SHORT).show();
                    Log.e("DatChangeError:", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errorConnect", String.valueOf(databaseError));
            }
        });
    }


    public void getAllGroup(ArrayList<Group> Grouplist) {
        ArrayList<Group> groups = Grouplist;
        Log.d("GroupAdapters", String.valueOf(Grouplist.size()));
        GroupListAdapter GroupAdapter = new GroupListAdapter(groups, GroupActivity.this);
        Log.d("GroupAdapters", String.valueOf(GroupAdapter.getItemCount()));
        recyclerView.setAdapter(GroupAdapter);
    }

    @Override
    public void onGroupItemClick(String groupId, String groupNum, String projName, boolean groupMe) {
        Toast.makeText(this, "Group: " + groupId, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, GroupDetailActivity.class);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        i.putExtra("puid", puid); // is 0 or 1
        i.putExtra("classid", classid);
        i.putExtra("class_puid", class_puid);
        i.putExtra("classname", classname);
        i.putExtra("groupid", groupId);
        i.putExtra("groupme", groupMe);
        i.putExtra("groupnum", groupNum);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListener_project != null) {
            projRef.removeEventListener(valueEventListener_project);
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
        //หน้ากรุ๊บไม่อยากให้มี memu bar
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
            Intent i = new Intent(GroupActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_group_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            uid = getI.getStringExtra("uid");
            Intent i = new Intent(GroupActivity.this, ClassroomActivity.class);
            i.putExtra("uid", uid);
            i.putExtra("ustatus", setNavHeader.ustatus);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_group_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(GroupActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
           // findViewById(R.id.inc_group_list).setVisibility(View.GONE);
           // finish();
        }  else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(GroupActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(GroupActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_group_list).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
