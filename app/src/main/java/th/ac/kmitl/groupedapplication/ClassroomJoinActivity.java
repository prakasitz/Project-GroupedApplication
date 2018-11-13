package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import th.ac.kmitl.groupedapplication.model.Classroom;

public class ClassroomJoinActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classRef;
    private DatabaseReference classRef_1;
    private ValueEventListener valueEventListener;
    private Query QuerybyClassID;

    private TextView tvEmail;
    private TextView tvFullName;
    private EditText edJoinClassID;
    private Button  btnSubmit;

    private String uid;
    private String ustatus;
    private int lastKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_join);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //-------------setView-------------------
        edJoinClassID = findViewById(R.id.editJoinClassID);
        btnSubmit = findViewById(R.id.btnSubmitJoinClass);
        //-------------setCurrentUser------------
        mAuth = FirebaseAuth.getInstance();
        //------------getIntent------------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        //------------setVisibility---------------
        findViewById(R.id.inc_classroom_join).setVisibility(View.VISIBLE);

        //-----------setOnclick----------------------------
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String joinClassID = edJoinClassID.getText().toString();
                Log.e("joinClassID",joinClassID);
                classRef_1 = database.getReference("classrooms");
                classRef = database.getReference("classrooms");
                if(joinClassID.length() == 1) {
                    QuerybyClassID = classRef_1.orderByKey().equalTo(joinClassID);
                    Log.w("1","yest");
                } else if(joinClassID.length() >= 5) {
                    QuerybyClassID = classRef_1.orderByKey();
                    Log.w("2","yest");
                }
                valueEventListener = QuerybyClassID.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Classkey;
                        int memberkey = 0;
                        for(DataSnapshot c : dataSnapshot.getChildren()) {
                            Classkey = c.getKey();
                           if(Classkey.indexOf(joinClassID) != -1) {//class id


                              for(DataSnapshot cc : c.getChildren()) {
                                  for(DataSnapshot cd : cc.getChildren()) {
                                        memberkey = Integer.parseInt(cd.getKey());
                                        Log.e("memberkey",String.valueOf(memberkey));
                                  }
                                  break;
                              }

                              String a = c.getValue().toString();
                               if(a.indexOf(uid) != -1) { //check string
                                  Toast.makeText(ClassroomJoinActivity.this,"คุณเข้าร่วมห้องเรียนนี้แล้ว",Toast.LENGTH_SHORT);
                               } else {
                                   Map<String, Object> stdJoin = new HashMap<>();
                                   stdJoin.put(String.valueOf(memberkey+1),String.valueOf(uid));
                                   classRef.child(Classkey).child("member_list").updateChildren(stdJoin);
                                   Toast.makeText(ClassroomJoinActivity.this,"ลงทะเบียสำเร็จ",Toast.LENGTH_SHORT);
                                   break;
                               }
                               break;
                           }
                        }

                        Intent i = new Intent(ClassroomJoinActivity.this , ClassroomActivity.class);
                        i.putExtra("uid",uid);
                        i.putExtra("ustatus",ustatus);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        findViewById(R.id.inc_classroom_join).setVisibility(View.GONE);
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //-----------------------------------------------------------
                try {
                    /*if(joinClassID.length() != 0) {
                        classRef = database.getReference("classrooms");
                        String classroomsKey = classRef.push().getKey().toString();
                        Map<String, Object> classTable = new HashMap<>();
                        classTable.put("", uid);
                    } else {
                        edJoinClassID.setError("กรุณากรองช่องนี้");
                    }*/
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
        if(ustatus.equals("0")) {
            Menu menubar = navigationView.getMenu();
            menubar.findItem(R.id.nav_classcreate).setVisible(false);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            classRef.removeEventListener(valueEventListener);
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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(ClassroomJoinActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_classroom_join).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            Intent intent = new Intent(ClassroomJoinActivity.this, ClassroomActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("ustatus", setNavHeader.ustatus);
            startActivity(intent);
            findViewById(R.id.inc_classroom_join).setVisibility(View.GONE);
            finish();
            //แค่ GONE หายไป
        } else if (id == R.id.nav_classcreate) {
            Toast.makeText(ClassroomJoinActivity.this,"คุณอยู่หน้านี้แล้ว",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(ClassroomJoinActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(ClassroomJoinActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_classroom_join).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
