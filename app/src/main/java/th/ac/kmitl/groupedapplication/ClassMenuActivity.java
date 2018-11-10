package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.controller.setShowCount;


public class ClassMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    //-------------View-----------------
    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvSubject;
    //------------String-------------------
    private Button btnAddProject;
    private Button btnViewProject;
    private Button btnViewGroup;
    private Button btnViewMember;
    //------------------------------
    private String uid;
    private String ustatus;
    private String classid;
    private String classname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ////-------------------coding----------------------
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        //------------setVisibility---------------
        findViewById(R.id.inc_classroom_menu).setVisibility(View.VISIBLE);
        //----------------view-----------------------
        tvSubject = findViewById(R.id.title_subject);
        btnAddProject = findViewById(R.id.btnAddProject);
        btnViewProject = findViewById(R.id.btnViewProject);
        btnViewGroup = findViewById(R.id.btnViewGroupStudent);
        btnViewMember = findViewById(R.id.btnViewMember);

        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");

        new setShowCount(btnViewMember, classid, "");
        new setShowCount(btnViewProject, classid, uid);

        tvSubject.setText(classname);

        if(ustatus != "" && ustatus.equals("1")) { //เป็นครู
            btnAddProject.setVisibility(View.VISIBLE);
            btnAddProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /*Intent i = new Intent(ClassMenuActivity.this, AddProjectActivity.class);
                    i.putExtra("uid", uid);
                    i.putExtra("classid", classid);*/
                }
            });

            btnViewProject.setVisibility(View.VISIBLE);
            btnViewProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ClassMenuActivity.this, ViewProjectActivity.class);
                    i.putExtra("uid", uid);
                    i.putExtra("classid", classid);
                    i.putExtra("classname", classname);
                }
            });

            btnViewMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*   Intent i = new Intent(ClassMenuActivity.this, ViewMemberActivity.class);
                i.putExtra("uid", uid);
                i.putExtra("classid", classid); */
                }
            });
        } else { //เป็นนักเรียน
            btnViewGroup.setVisibility(View.VISIBLE);
            btnViewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ClassMenuActivity.this,"คุณเป็นนักเรียน!",Toast.LENGTH_SHORT).show();
                 /*   Intent i = new Intent(ClassMenuActivity.this, ViewGroupActivity.class);
                    i.putExtra("uid", uid);
                    i.putExtra("classid", classid); */
                }
            });

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) btnViewMember.getLayoutParams();
            params.setMargins(
                    ((ConstraintLayout.LayoutParams) btnViewMember.getLayoutParams()).leftMargin,
                    ((ConstraintLayout.LayoutParams) btnViewMember.getLayoutParams()).topMargin+300,
                    ((ConstraintLayout.LayoutParams) btnViewMember.getLayoutParams()).rightMargin,
                    ((ConstraintLayout.LayoutParams) btnViewMember.getLayoutParams()).bottomMargin
            );

            btnViewMember.setLayoutParams(params);

            btnViewMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ClassMenuActivity.this,"คุณเป็นนักเรียน!: "+
                            btnViewMember.getTop(),Toast.LENGTH_SHORT).show();
             /*   Intent i = new Intent(ClassMenuActivity.this, ViewMemberActivity.class);
                i.putExtra("uid", uid);
                i.putExtra("classid", classid); */
                }
            });
        }



        //ยังไม่เสร็จ


        //--------------------end------------------------------------------
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
    }

    //-------------template----------------------
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
        //-------View-------
        tvEmail = findViewById(R.id.textEmail);
        tvFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid, tvEmail, tvFullName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_createClassroom) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //---------getIntent----------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        //---------getItem----------
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(ClassMenuActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_classroom_menu).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_classcreate) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) { //---logout
            mAuth.signOut();
            Toast.makeText(ClassMenuActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ClassMenuActivity.this, LoginActivity.class);
            startActivity(intent);
            findViewById(R.id.inc_classroom_menu).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
