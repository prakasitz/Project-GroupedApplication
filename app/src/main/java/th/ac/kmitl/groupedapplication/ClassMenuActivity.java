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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ClassMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextView textEmail;
    private TextView textFullName;
    private Button btnAddWork;
    private Button btnViewWork;
    private Button btnViewGroup;
    private Button btnViewMember;

    private String uid;
    private  String ustatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        findViewById(R.id.inc_classroom_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.inc_class).setVisibility(View.GONE);
        findViewById(R.id.inc_profile).setVisibility(View.GONE);

        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");


        if(true) { //เป็นครู
            btnAddWork = findViewById(R.id.btnAddWork);
            btnViewWork = findViewById(R.id.btnViewWork);

            btnAddWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            btnViewWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else { //เป็นนักเรียน
            btnViewGroup = findViewById(R.id.btnViewGroupStudent);

            btnViewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        btnViewMember = findViewById(R.id.btnViewMember);

        btnViewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //ยังไม่เสร็จ



        /// /
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
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
        mAuth = FirebaseAuth.getInstance();
        textEmail = findViewById(R.id.textEmail);
        textFullName = findViewById(R.id.textFullName);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent getI = getIntent();
                uid = getI.getStringExtra("uid");
                DataSnapshot usersPathEmail = dataSnapshot.child("users/"+uid+"/email");
                DataSnapshot usersPathStatus = dataSnapshot.child("users/"+uid+"/status");
                String status_str;

                if(usersPathStatus.getValue() != null) {
                    ustatus = String.valueOf(usersPathStatus.getValue());
                    switch (ustatus) {
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
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(ClassMenuActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Toast.makeText(ClassMenuActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ClassMenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
