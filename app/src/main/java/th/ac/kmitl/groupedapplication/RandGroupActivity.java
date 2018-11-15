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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Collections;

import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.controller.setShowCount;

public class RandGroupActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private TextView tvQty;
    private TextView tvSubject;
    private TextView tvEmail;
    private TextView tvFullName;
    private Integer radioValue = 2;
    private RadioButton g2,g3,g4,g5,g6;
    private Button btn_setGroup;


    ArrayList<String> userInClass = new ArrayList<String>();
    ArrayList<String> modQty = new ArrayList<String>();
    Map<String,Object> GroupInfoUpdate = new HashMap<>();

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference classRef;
    private DatabaseReference groupRef;
    private ValueEventListener valueEventListener_class;

    private int countGroup;
    private int countUser;
    private String uid;
    private String ustatus;
    private String classid;
    private String classname;
    private boolean chkRandGroup;
    protected static boolean chkMember = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randgroup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //----------mAth--------------------
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //---------View----------------------
        tvSubject = findViewById(R.id.title_class_in_rand_group);
        tvQty = findViewById(R.id.tvQty);
        g2 = findViewById(R.id.radio_g2);
        g3 = findViewById(R.id.radio_g3);
        g4 = findViewById(R.id.radio_g4);
        g5 = findViewById(R.id.radio_g5);
        g6 = findViewById(R.id.radio_g6);
        btn_setGroup = findViewById(R.id.btn_setGroup);
        //-------------getIntent-------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
        //------------setVisibility---------------
        findViewById(R.id.inc_rand_group).setVisibility(View.VISIBLE);
        //------------setText----------------
        tvSubject.setText(classname);
        //-----------setOnclick---------------
        g2.setOnClickListener(this);
        g3.setOnClickListener(this);
        g4.setOnClickListener(this);
        g5.setOnClickListener(this);
        g6.setOnClickListener(this);
        btn_setGroup.setOnClickListener(this);
        //-----------coding---------------------
        classRef = database.getReference("classrooms");
        valueEventListener_class = classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot userData = dataSnapshot.child(classid+"/"+"member_list");
                Log.wtf("userData", String.valueOf(userData));
                if(userData.getValue() != null) {
                    countUser = Integer.parseInt(String.valueOf(userData.getChildrenCount()));
                } else {
                    Log.e("NoStudent","ไม่มีนักศึกษา");
                    Toast.makeText(RandGroupActivity.this,"ไม่มีสมาชิกในห้องเรียนนี้ ลองใหม่อีกครั้ง",Toast.LENGTH_LONG).show();
                    chkMember = false;
                    finish();
                }
                Log.wtf("countData",  String.valueOf(countUser));

                for(DataSnapshot userAcc : userData.getChildren()){
                    userInClass.add(String.valueOf(userAcc.getValue()));
                    Log.wtf("userInArray",String.valueOf(userAcc.getKey()+" :  "+ String.valueOf(userAcc.getValue())));

                }

                g2.setText("2 คน/กลุ่ม เหลือ "+userInClass.size()%2 );
                g3.setText("3 คน/กลุ่ม เหลือ "+userInClass.size()%3 );
                g4.setText("4 คน/กลุ่ม เหลือ "+userInClass.size()%4 );
                g5.setText("5 คน/กลุ่ม เหลือ "+userInClass.size()%5 );
                g6.setText("6 คน/กลุ่ม เหลือ "+userInClass.size()%6 );

                Log.wtf("ArrayList",String.valueOf(userInClass));
                Collections.shuffle(userInClass);
                Log.wtf("ArrayList",String.valueOf(userInClass));

                for(int num = 2; num<= 6 ;num++) {
                    //modQty.add(String.valueOf(num));
                    if(userInClass.size() % num == 0) {

                        Log.wtf("mod",String.valueOf(num)+"/// 0");
                    }else {
                        Log.wtf("mod",String.valueOf(num)+"/// "+userInClass.size()%num );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("errorConnect", String.valueOf(databaseError));
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
        if (valueEventListener_class != null) {
            classRef.removeEventListener(valueEventListener_class);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.radio_g2) {
            radioValue = 2;
            Toast.makeText(RandGroupActivity.this, "2", Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.radio_g3){
            radioValue = 3;
            Toast.makeText(RandGroupActivity.this, "3", Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.radio_g4){
            radioValue = 4;
            Toast.makeText(RandGroupActivity.this, "4", Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.radio_g5){
            radioValue = 5;
            Toast.makeText(RandGroupActivity.this, "5", Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.radio_g6){
            radioValue = 6;
            Toast.makeText(RandGroupActivity.this, "6", Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.btn_setGroup){
            groupRef = database.getReference("group_list");
            groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int num = 0;
                    int plus = 1;
                    DataSnapshot groupList = dataSnapshot;
                    countGroup = Integer.parseInt(String.valueOf(groupList.getChildrenCount()));
                    //countUser;
                    //radioValue;
                    Log.wtf("countGroup",  String.valueOf(countGroup));
                    for(DataSnapshot inGroup : groupList.getChildren()) {
                        boolean hasClassID = inGroup.hasChild("class_id");
                        Log.e("hasclass_id",hasClassID+"_");
                        Log.e("childrenGroup",groupList.toString());
                        if(hasClassID && inGroup.child("class_id").getValue().toString().equals(classid)) {
                            chkRandGroup = false;
                            break;
                        } else {
                            chkRandGroup = true;
                        }
                    }

                    if(chkRandGroup) {
                        for (int i = 0; i < userInClass.size(); i++) {
                            GroupInfoUpdate.put((countGroup + plus) + "/member_list/" + (num + 1), userInClass.get(i));
                            num++;
                            if (num == radioValue) {
                                num = 0;
                                GroupInfoUpdate.put((countGroup + plus) + "/class_id", classid);
                                plus++;
                            }
                        }
                        if (num != 0) {
                            GroupInfoUpdate.put((countGroup + plus) + "/class_id", classid);
                        }
                        groupRef.updateChildren(GroupInfoUpdate);
                        btn_setGroup.setEnabled(false);
                        finish();
                    } else {
                        Toast.makeText(RandGroupActivity.this,"ชั้นเรียนนี้ได้มีการจัดกลุ่มเอาไว้แล้ว...",Toast.LENGTH_SHORT).show();
                        Log.e("NoWay","OK");
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("errorConnect", String.valueOf(databaseError));
                }
            });

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(RandGroupActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
            findViewById(R.id.inc_rand_group).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            Intent i = new Intent(RandGroupActivity.this, ClassroomActivity.class);
            i.putExtra("uid", uid);
            i.putExtra("ustatus", setNavHeader.ustatus);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_rand_group).setVisibility(View.GONE);
            finish();
            //แค่ GONE หายไป
        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(RandGroupActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //  findViewById(R.id.inc_project_list).setVisibility(View.GONE);
            // finish();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(RandGroupActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(RandGroupActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_rand_group).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
