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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import th.ac.kmitl.groupedapplication.adapter.GroupItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.GroupListAdapter;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Group;
import th.ac.kmitl.groupedapplication.model.Member;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , GroupItemClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference groupRef;
    private DatabaseReference projRef;
    private ChildEventListener childEventListener_proj;
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
        //--------------mthu---------------------
        mAuth = FirebaseAuth.getInstance();
        ///--------------recycleview--------------------------
        recyclerView = findViewById(R.id.recyclerView_Group);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ////-------------------coding----------------------
        //------------setVisibility---------------
        findViewById(R.id.inc_group_list).setVisibility(View.VISIBLE);
        //----------------view-----------------------
        tvSubject = findViewById(R.id.title_class_in_group_list);
        //-------รับค่าจากหน้า MenuStudent ต้องเอา puid มาให้ได้อะ , MenuTeacher ส่งได้ไม่มีปัญหา
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        puid = getI.getStringExtra("puid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
        class_puid = classid + "_" + puid;
        //------------------setText subject--------------------------
        tvSubject.setText(classname);
        //----------------------draw nav bar--------------------------------
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
        getDataResults(ustatus,class_puid);
    }



    public void getDataResults(final String uStatus, final  String Class_puID) {
        final ArrayList<Group> groupArrayList = new ArrayList<Group>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        groupRef = database.getReference("group_list");
        projRef = database.getReference("project_list");
        Log.e("test","hello + class_puID::"+Class_puID);
        try { //อันนี้ ได้ทั้งอาจารย์และนักเรียนเลย
             projRef.orderByChild("class_uid").equalTo(Class_puID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final String projID = dataSnapshot.getKey();
                    final String projName = dataSnapshot.child("proj_name").getValue().toString();
                    Log.e("proj",dataSnapshot.toString());
                    Log.e("projID :: projName",projID + "_"+projName);
                    groupRef.orderByChild("proj_id").equalTo(projID).addChildEventListener(new ChildEventListener() {
                        int groupNum = 0;
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.e("testGroupRef",dataSnapshot.toString());
                            String groupID = dataSnapshot.getKey();
                            String groupMe = "+";
                            groupNum = groupNum +1;
                            String groupCount =  String.valueOf(dataSnapshot.child("member_list").getChildrenCount());
                            for (DataSnapshot findME: dataSnapshot.child("member_list").getChildren()) {
                                if(findME.getValue().toString().equals(uid)) {
                                    groupMe = "(ฉัน)";
                                    Log.e(">>>>>>Me in group!",">>>>>ME,yes<<<<<<");
                                    break;
                                }
                            }
                            Group groupFull = new Group(groupID, groupCount, groupMe, String.valueOf(groupNum), projName, projID);
                            groupArrayList.add(groupFull);
                            getAllGroup(groupArrayList);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        } catch (Exception e) {
            Log.e("queryFail D: ", e.toString());
        }

    }


    public void getAllGroup(ArrayList<Group> Grouplist) {
        ArrayList<Group> groups = Grouplist;
        Log.d("GroupAdapters", String.valueOf(Grouplist.size()));
        GroupListAdapter GroupAdapter = new GroupListAdapter(groups, GroupActivity.this);
        Log.d("GroupAdapters", String.valueOf(GroupAdapter.getItemCount()));
        recyclerView.setAdapter(GroupAdapter);
    }

    @Override
    public void onGroupItemClick(String groupId, String projectID, String projectName) {
        Toast.makeText(this, "MemberID : " + groupId, Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, MemberDetailActivity.class);
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        i.putExtra("classid", MemberID);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        startActivity(i);*/
    }

    @Override
    public void onStart() {
        super.onStart();
        //projRef.addChildEventListener(childEventListener_proj);
        //groupRef.addChildEventListener(childEventListener_group);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener_proj != null || childEventListener_proj != null) {
           // projRef.removeEventListener(childEventListener_proj);
           // groupRef.removeEventListener(childEventListener_group);
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
        /*if (!ustatus.equals("0")) {
            getMenuInflater().inflate(R.menu.main, menu);
        }*/
        //---------nav head-----------------
        tvEmail = findViewById(R.id.textEmail);
        tvFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid, tvEmail, tvFullName);
        Log.e("CreateOpMenu", "ok");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       /* if (id == R.id.action_createClassroom) {
            Intent i = new Intent(GroupActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_member_list).setVisibility(View.GONE);
            finish();
            return true;
        }*/

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
            startActivity(i);
            findViewById(R.id.inc_group_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(GroupActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_group_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
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
