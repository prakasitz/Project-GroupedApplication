package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import th.ac.kmitl.groupedapplication.controller.InsertforProf;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Classroom;

public class ClassroomCreateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classRef;
    private ChildEventListener childEventListener;
    private Query QuerybyUID;

    private TextView tvEmail;
    private TextView tvFullName;
    private EditText edsubjectName;
    private Button  btnSubmit;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_create);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //-------------setView-------------------
        edsubjectName = findViewById(R.id.editSubjName);
        btnSubmit = findViewById(R.id.btnClassCreate);
        //-------------setCurrentUser------------
        mAuth = FirebaseAuth.getInstance();
        //------------getIntent------------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        //------------setVisibility---------------
        findViewById(R.id.inc_class_create).setVisibility(View.VISIBLE);
        //-----------setOnclick----------------------------
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjName = edsubjectName.getText().toString();
                try {
                    if(subjName.length() != 0) {
                        classRef = database.getReference("classrooms");
                        String classroomsKey = classRef.push().getKey().toString();
                        Map<String, Object> classTable = new HashMap<>();
                        classTable.put("p_uid", uid);
                        classTable.put("subj_name", subjName);
                        Log.e("classroomsKey",classroomsKey);

                        new InsertforProf( //สำหรับ Insert และ check ว่าเข้าใหม่ พร้อมส่ง intent uid และ ustatus ไปหน้าที่ต้องการ ระบบแค่ uid พอ
                                uid,
                                classroomsKey,
                                classRef,
                                classTable,
                                getApplicationContext(),
                                ClassroomActivity.class,
                                ClassroomCreateActivity.this
                        );
                        Toast.makeText(ClassroomCreateActivity.this, "สร้างห้องสำเร็จ!",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ClassroomCreateActivity.this, ClassroomActivity.class);
                        i.putExtra("ustatus","1");
                        i.putExtra("uid",uid);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    } else {
                        edsubjectName.setError("กรุณากรองช่องนี้");
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
        navigationView.setCheckedItem(R.id.nav_classcreate);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener != null) {
            classRef.removeEventListener(childEventListener);
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
        //---------nav head-----------------
        tvEmail = findViewById(R.id.textEmail);
        tvFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid,tvEmail,tvFullName);
        Log.e("CreateOpMenu","ok");
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
            Intent i = new Intent(ClassroomCreateActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_class_create).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            //หน้า สร้างคลาส กดตรงนี้แล้วจะ finish ตัวเองไป
            Intent i = new Intent(ClassroomCreateActivity.this, ClassroomActivity.class);
            i.putExtra("uid", uid);
            i.putExtra("ustatus", setNavHeader.ustatus);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(i);
            //findViewById(R.id.inc_class_create).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classcreate) {
            Toast.makeText(ClassroomCreateActivity.this,"คุณอยู่หน้านี้แล้ว",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(ClassroomCreateActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(ClassroomCreateActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_class_create).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
