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
import th.ac.kmitl.groupedapplication.adapter.classroomListAdapter;
import th.ac.kmitl.groupedapplication.model.Classroom;

public class classroomActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, ClassroomItemClickListener {


    private String uid = null;
    private TextView textEmail;
    private TextView textFullName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    public DatabaseReference myRef;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
    }



    public ArrayList<Classroom> getAllContacts(){
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");

        database = FirebaseDatabase.getInstance();
        ArrayList<Classroom> classroomArrayList = new ArrayList<Classroom>();
        myRef = database.getReference("classroom/"+uid);

        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String classID = dataSnapshot.getKey();

                Log.d("getkey",classID);
                String classSubject = dataSnapshot.child("class_subject").getValue().toString();
                Classroom  c  = new Classroom(classID,classSubject);
                classroomArrayList.add(c);
                Log.d("get",c.getClass_subject());
                Log.d("size",String.valueOf(classroomArrayList.size()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("NEW",String.valueOf(classroomArrayList.size()));
        return classroomArrayList;
    }

    @Override
    protected void onStart(){
        super.onStart();
        classroomListAdapter classroomAdapter = new classroomListAdapter(getAllContacts(), classroomActivity.this);
        recyclerView.setAdapter(classroomAdapter);
    }

    @Override
        public void onClassroomItemClick(String classID){
        Toast.makeText(this, "Class Id: "+ classID, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ClassMenuActivity.class);
        i.putExtra("classID", classID);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        textEmail = findViewById(R.id.textEmail);
        textFullName = findViewById(R.id.textFullName);

        database = FirebaseDatabase.getInstance();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("LogIn","logon");
                Intent i = getIntent();
                uid = String.valueOf(i.getStringExtra("uid"));
                Log.d("LogIn",uid);
                DataSnapshot usersPathEmail = dataSnapshot.child("users/"+uid+"/email");
                DataSnapshot usersPathStatus = dataSnapshot.child("users/"+uid+"/status");
                String status_str = null;

                if(usersPathStatus.getValue() != null) {
                    String status = String.valueOf(usersPathStatus.getValue());
                    switch (status) {
                        case "1": status_str = " (อาจารย์)";
                            DataSnapshot ProfessorPath = dataSnapshot.child("professor/"+uid);
                            Log.d("userUsers",String.valueOf(usersPathEmail));
                            Log.d("userProf",String.valueOf(ProfessorPath));
                            textEmail.setText(String.valueOf(usersPathEmail.getValue()));
                            textFullName.setText(String.valueOf(
                                    ProfessorPath.child("prof_name").getValue() + " " +
                                            ProfessorPath.child("prof_lname").getValue() + status_str ));
                            break;
                        case "0": status_str = " (นักเรียน)";
                            DataSnapshot studentsPath = dataSnapshot.child("student/"+uid);
                            Log.d("userUsers",String.valueOf(usersPathEmail));
                            Log.d("userDStudents",String.valueOf(studentsPath));
                            textEmail.setText(String.valueOf(usersPathEmail.getValue()));
                            textFullName.setText(String.valueOf(
                                    studentsPath.child("std_name").getValue() + " " +
                                            studentsPath.child("std_lname").getValue() + status_str ));
                            break;
                        default:
                            break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errorConnect",String.valueOf(databaseError));
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent getI = getIntent();
            uid = getI.getStringExtra("uid");

            Intent intent = new Intent(classroomActivity.this, profileActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Toast.makeText(classroomActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(classroomActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
