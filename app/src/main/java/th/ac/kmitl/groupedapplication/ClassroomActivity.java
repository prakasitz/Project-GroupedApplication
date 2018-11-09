package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import th.ac.kmitl.groupedapplication.adapter.ClassroomItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.ClassroomListAdapter;
import th.ac.kmitl.groupedapplication.model.Classroom;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;

public class ClassroomActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, ClassroomItemClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ChildEventListener childEventListener;

    private TextView textEmail;
    private TextView textFullName;
    private RecyclerView recyclerView;

    private String uid;
    private  String ustatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //------------setVisibility---------------
        findViewById(R.id.inc_class).setVisibility(View.VISIBLE);
        //----------------decrea recyclerview------------------------
        recyclerView = findViewById(R.id.recyclerViewClassroom);
        recyclerView.setHasFixedSize(true);
        //----------------set layout-----------------------
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //-------------create nav------------
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
        //-------------class data set to recycleview------------
        getDataResults();
    }

    public void getDataResults(){
        final ArrayList<Classroom> classroomArrayList = new ArrayList<Classroom>();
        //------------get uid-----------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        //------------Firebase-----------------
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("classrooms");
        childEventListener =
                myRef.orderByChild("p_uid").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("getData",dataSnapshot.toString());
                String classID = dataSnapshot.getKey();
                String classSubject = dataSnapshot.child("subj_name").getValue().toString();
                Classroom  classroom  = new Classroom(classID,classSubject);
                classroomArrayList.add(classroom);
                getAllClassroom(classroomArrayList);
                Log.d("getkey",classID);
                Log.d("get",classroom.getClass_subject());
                Log.d("size",String.valueOf(classroomArrayList.size()));
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

    public void getAllClassroom(ArrayList<Classroom> classroomlist) {
        ArrayList<Classroom> classrooms = classroomlist;
        ClassroomListAdapter classroomAdapter = new ClassroomListAdapter(classrooms, ClassroomActivity.this);
        Log.d("cList",String.valueOf(classrooms.toArray()));
        recyclerView.setAdapter(classroomAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener != null) {
            myRef.removeEventListener(childEventListener);
        }
    }


    @Override
    public void onClassroomItemClick(String classID,String className){
        Toast.makeText(this, "Class Id: "+ classID, Toast.LENGTH_SHORT).show();
        //-------------intent ไป ClassMenuActivity----------
        Intent i = new Intent(this, ClassMenuActivity.class);
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        i.putExtra("classid", classID);
        i.putExtra("classname", className);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //---------nav head-----------------
        textEmail = findViewById(R.id.textEmail);
        textFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid,textEmail,textFullName);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
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
            Intent i = new Intent(ClassroomActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_class).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(ClassroomActivity.this, LoginActivity.class);
            startActivity(i);
            Toast.makeText(ClassroomActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_class).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
