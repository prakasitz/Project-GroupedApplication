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

import th.ac.kmitl.groupedapplication.controller.setNavHeader;


public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TextView userID;
    private EditText userName;
    private EditText userLname;
    private EditText userStatus;
    private TextView tvEmail;
    private TextView tvFullName;
    private Button btnSubmit;

    public DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String ustatus;
    private  String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //-----------setVisable----------------------
        findViewById(R.id.inc_profile).setVisibility(View.VISIBLE);

        //-----------View----------------------
        userID = findViewById(R.id.user_head_temp);
        userName = findViewById(R.id.edit_name);
        userLname = findViewById(R.id.edit_lastname);
        userStatus = findViewById(R.id.edit_status);
        btnSubmit = findViewById(R.id.Submitbutton);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> AccountUpdate = new HashMap<>();
                AccountUpdate.put("fname", String.valueOf(userName.getText()));
                AccountUpdate.put("lname", String.valueOf(userLname.getText()));
                AccountUpdate.put("post", String.valueOf(userStatus.getText()));
                myRef.child(uid).updateChildren(AccountUpdate);
                Toast.makeText(ProfileActivity.this, "บันทึกข้อมูลเรียบร้อย:" + userName.getText()+" "+
                                                                                    userLname.getText()+" "+
                                                                                    userStatus.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent i = getIntent();
                uid = String.valueOf(i.getStringExtra("uid"));
                Log.d("LogIn","logon");
                Log.d("LogIn",uid);
                DataSnapshot usersLevel= dataSnapshot.child(uid+"/level");
                DataSnapshot userData = dataSnapshot.child(uid);
                Log.d("userData", String.valueOf(userData));
                    for (DataSnapshot userAcc : userData.getChildren()) { //ค่าออกมาเป๋นแต่ละ key
                        switch (userAcc.getKey()) {
                            case "id":
                                userID.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "fname":
                                userName.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "lname":
                                userLname.setText(String.valueOf(userAcc.getValue()));
                                break;
                            case "post":
                                userStatus.setText(String.valueOf(userAcc.getValue()));
                                break;
                            default:
                                System.out.println(userAcc.getKey());
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errorConnect",String.valueOf(databaseError));
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);
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

        if (id == R.id.action_createClassroom) {
            Intent i = new Intent(ProfileActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_profile).setVisibility(View.GONE);
            finish();
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
            Intent intent = new Intent(ProfileActivity.this, ClassroomActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("ustatus", setNavHeader.ustatus);
            startActivity(intent);
            findViewById(R.id.inc_profile).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(ProfileActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_profile).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            findViewById(R.id.inc_profile).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
