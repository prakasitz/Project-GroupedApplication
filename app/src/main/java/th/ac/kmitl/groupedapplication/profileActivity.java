package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
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


public class profileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public DatabaseReference myRef;
    private TextView userID;
    private EditText userName;
    private EditText userLname;
    private EditText userStatus;
    private TextView textEmail;
    private TextView textFullName;
    private Button btnSubmit;
    private Button btnCancel;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String status_str;


    private  String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.inc_class).setVisibility(View.GONE);
        findViewById(R.id.inc_profile).setVisibility(View.VISIBLE);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userID = findViewById(R.id.user_head_temp);
        userName = findViewById(R.id.edit_name);
        userLname = findViewById(R.id.edit_lastname);
        userStatus = findViewById(R.id.edit_status);
        btnSubmit = findViewById(R.id.Submitbutton);
        btnCancel = findViewById(R.id.Cancelbutton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> AccountUpdate = new HashMap<>();
                AccountUpdate.put("std_name", String.valueOf(userName.getText()));
                AccountUpdate.put("std_lname", String.valueOf(userLname.getText()));
                AccountUpdate.put("std_post", String.valueOf(userStatus.getText()));
                myRef.child("student/"+uid).updateChildren(AccountUpdate);
                Toast.makeText(profileActivity.this, "บันทึกข้อมูลเรียบร้อย:"+uid+" "+userName+" "+userLname+" "+userStatus, Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent i = getIntent();
                uid = String.valueOf(i.getStringExtra("uid"));
                Log.d("LogIn","logon");
                Log.d("LogIn",uid);
                DataSnapshot usersStatus = dataSnapshot.child("users/"+uid+"/status");
                if(String.valueOf(usersStatus) == "0") {
                    DataSnapshot userData = dataSnapshot.child("student/"+uid);
                    Log.d("userData",String.valueOf(userData));
                    for (DataSnapshot userAcc : userData.getChildren()) { //ค่าออกมาเป๋นแต่ละ key
                        switch (userAcc.getKey()) {
                            case "std_id":
                                userID.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "std_name":
                                userName.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "std_lname":
                                userLname.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "std_post":
                                userStatus.setText(String.valueOf(userAcc.getValue()));
                                break;
                            default: System.out.println(userAcc.getKey());
                        }
                    }
                } else {
                    DataSnapshot userData = dataSnapshot.child("professor/"+uid);
                    Log.d("userData",String.valueOf(userData));
                    for (DataSnapshot userAcc : userData.getChildren()) { //ค่าออกมาเป๋นแต่ละ key
                        switch (userAcc.getKey()) {
                            case "prof_id":
                                userID.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "prof_name":
                                userName.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "prof_lname":
                                userLname.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "prof_post":
                                userStatus.setText(String.valueOf(userAcc.getValue()));
                                break;
                            default: System.out.println(userAcc.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errorConnect",String.valueOf(databaseError));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        textEmail = findViewById(R.id.textEmail);
        textFullName = findViewById(R.id.textFullName);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("LogIn","logon");
                Intent i = getIntent();
                uid = String.valueOf(i.getStringExtra("uid"));
                Log.d("LogIn",uid);
                DataSnapshot usersPathEmail = dataSnapshot.child("users/"+uid+"/email");
                DataSnapshot usersPathStatus = dataSnapshot.child("users/"+uid+"/status");

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

        } else if (id == R.id.nav_classroom) {
            Intent getI = getIntent();
            uid = getI.getStringExtra("uid");
            Intent intent = new Intent(profileActivity.this, classroomActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Toast.makeText(profileActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(profileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
