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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import th.ac.kmitl.groupedapplication.controller.InsertforProf;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;

public class ProjectAddActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference projRef;
    private ValueEventListener valueEventListener;
    private Query QuerybyUID;

    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvSubject;
    private EditText edProjectName;
    private Button btnSubmit;

    private String uid;
    private String classid;
    private String classname;
    private int lastKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //------------setVisibility---------------
        findViewById(R.id.inc_project_add).setVisibility(View.VISIBLE);
        //-------------setView-------------------
        tvSubject = findViewById(R.id.title_class_in_project);
        edProjectName = findViewById(R.id.edProjectName);
        btnSubmit = findViewById(R.id.btnSubmitAddproject);
        //-------------setCurrentUser------------
        mAuth = FirebaseAuth.getInstance();
        //------------getIntent------------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
        tvSubject.setText(classname);
        //-----------setOnclick----------------------------

        projRef = database.getReference("project_list");
        valueEventListener = projRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot cur: dataSnapshot.getChildren()) {
                    lastKey = Integer.parseInt(cur.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String subjName = edProjectName.getText().toString();
                try {
                    lastKey =  lastKey + 1;
                    if(subjName.length() != 0) {
                        final String projkey = String.valueOf(lastKey);

                        Map<String, Object> projectTable = new HashMap<>();
                        projectTable.put("p_uid", uid);
                        projectTable.put("class_uid", classid+"_"+uid);
                        projectTable.put("classroom_id", classid);
                        projectTable.put("proj_name", subjName);

                        new InsertforProf( //สำหรับ Insert และ check ว่าเข้าใหม่ พร้อมส่ง intent uid และ ustatus ไปหน้าที่ต้องการ ระบบแค่ uid พอ
                                uid,
                                projkey,
                                classid,
                                classname,
                                projRef,
                                projectTable,
                                getApplicationContext(),
                                ClassMenuActivity.class,
                                ProjectAddActivity.this
                        );

                    } else {
                        edProjectName.setError("กรุณากรองช่องนี้");
                    }

                } catch (Exception e) {
                    Log.e("errorOnclick",e.toString());
                }
            }
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

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            projRef.removeEventListener(valueEventListener);
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
        //getMenuInflater().inflate(R.menu.main, menu);
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
            Toast.makeText(ProjectAddActivity.this,"คุณอยู่หน้านี้แล้ว",Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(ProjectAddActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_project_add).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            Intent intent = new Intent(ProjectAddActivity.this, ClassroomActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("ustatus", setNavHeader.ustatus);
            startActivity(intent);
            findViewById(R.id.inc_project_add).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(ProjectAddActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_project_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(ProjectAddActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(ProjectAddActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_project_add).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
