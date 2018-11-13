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

import th.ac.kmitl.groupedapplication.adapter.MemberItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.MemberListAdapter;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Member;
import th.ac.kmitl.groupedapplication.model.Project;

public class MemberActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MemberItemClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference memRef;
    private DatabaseReference userRef;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //--------------mthu---------------------
        mAuth = FirebaseAuth.getInstance();
        ///--------------recycleview--------------------------
        recyclerView = findViewById(R.id.recyclerView_Member);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ////-------------------coding----------------------////
        Log.i("test", "hello");
        //------------setVisibility---------------
        findViewById(R.id.inc_member_list).setVisibility(View.VISIBLE);
        //----------------set view--------------------
        tvSubject = findViewById(R.id.title_class_in_member_list);
        //----------------intent------------------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
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

        getDataResults(classid, ustatus);
    }

    public void getDataResults(final String classID, final String uStatus) {
        final ArrayList<Member> memberArrayList = new ArrayList<Member>();
        database = FirebaseDatabase.getInstance();
        memRef = database.getReference("classrooms/" + classID);
        memRef.child("member_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String std_id;
                    for (DataSnapshot getUIDINmember : dataSnapshot.getChildren()) {
                        std_id = getUIDINmember.getValue().toString();
                        Log.e("std_id", std_id);

                        userRef = database.getReference("users");
                        userRef.child(std_id).addValueEventListener(new ValueEventListener() {
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
        Log.d("MemberAdapters", String.valueOf(Memberlist.size()));
        MemberListAdapter MemberAdapter = new MemberListAdapter(members, MemberActivity.this);
        Log.d("MemberAdapter", String.valueOf(MemberAdapter.getItemCount()));
        recyclerView.setAdapter(MemberAdapter);
    }

    @Override
    public void onMemberItemClick(String memID) {
        Toast.makeText(this, "MemberID : " + memID, Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, MemberDetailActivity.class);
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        i.putExtra("classid", MemberID);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        startActivity(i);*/
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
        if (!ustatus.equals("0")) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
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

        if (id == R.id.action_createClassroom) {
            Intent i = new Intent(MemberActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_member_list).setVisibility(View.GONE);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(MemberActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_member_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(MemberActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_member_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(MemberActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(MemberActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_member_list).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}