package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.database.Query;

import java.util.ArrayList;

import th.ac.kmitl.groupedapplication.adapter.ProjectItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.ProjectListAdapter;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Project;

public class ProjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProjectItemClickListener {
    //project use addChildEventListener
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference projRef;
    private ChildEventListener childEventListener_proj;
    //-------------View-----------------
    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvSubject;
    private RecyclerView recyclerView;
    //------------String-----------------
    private String uid;
    private String ustatus;
    private String classid;
    private String classname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //--------------mthu---------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        //--------------recycleview--------------------------------------------------------
        recyclerView = findViewById(R.id.recyclerView_Project);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        //-------------------coding---------------------------------------------------------
        Log.i("test", "hello");
        //------------setVisibility---------------------------------------------------------
        findViewById(R.id.inc_project_list).setVisibility(View.VISIBLE);
        //----------------set view-----------------------------------------------------------
        tvSubject = findViewById(R.id.title_class_in_project_list);
        //----------------intent-------------------------------------------------------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        classname = getI.getStringExtra("classname");
        //------------------setText subject--------------------------------------------------
        tvSubject.setText(classname);
        //----------------------draw nav bar--------------------------------------------------
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);

        getDataResults();
    }

    public void getDataResults() {
        final ArrayList<Project> projectArrayList = new ArrayList<Project>();
        database = FirebaseDatabase.getInstance();
        projRef = database.getReference("project_list");
        Query projQuery = projRef.orderByChild("class_uid").equalTo(classid+"_"+uid);
        childEventListener_proj = projQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    Log.d("key_proj", dataSnapshot.getKey());
                    String ProjectID = dataSnapshot.getKey();
                    String ProjectName = dataSnapshot.child("proj_name").getValue().toString();
                    Project project = new Project(ProjectID, ProjectName);
                    projectArrayList.add(project);
                    getAllProject(projectArrayList);
                    Log.d("get", project.getProject_name());
                    Log.d("size", String.valueOf(projectArrayList.size()));
                } catch (Exception e) {
                    Toast.makeText(ProjectActivity.this,"ไม่มีข้อมูล! Error!",Toast.LENGTH_SHORT).show();
                    Log.e("firebaseChildEror",e.toString());
                }
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

    public void getAllProject(ArrayList<Project> Projectlist) {
        ArrayList<Project> projects = Projectlist;
        Log.d("ProjectAdapters", String.valueOf(Projectlist.size()));
        ProjectListAdapter ProjectAdapter = new ProjectListAdapter(projects, ProjectActivity.this);
        Log.d("ProjectAdapter", String.valueOf(ProjectAdapter.getItemCount()));
        recyclerView.setAdapter(ProjectAdapter);
    }

    @Override
    public void onProjectItemClick(String projID) {
        Toast.makeText(this, "ProjectID : " + projID, Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, ProjectDetailActivity.class);
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        i.putExtra("classid", ProjectID);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        startActivity(i);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener_proj != null) {
            projRef.removeEventListener(childEventListener_proj);
        }
    }

    //-------------template----------------------
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
        // Handle navigation view item clicks here.
        //---------getIntent----------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        //---------getItem----------
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(ProjectActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_project_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            uid = getI.getStringExtra("uid");
            Intent i = new Intent(ProjectActivity.this, ClassroomActivity.class);
            i.putExtra("uid", uid);
            i.putExtra("ustatus", setNavHeader.ustatus);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_project_list).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(ProjectActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //findViewById(R.id.inc_project_list).setVisibility(View.GONE);
            //finish();
        } else if (id == R.id.nav_logout) { //---logout
            mAuth.signOut();
            Toast.makeText(ProjectActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ProjectActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            findViewById(R.id.inc_project_list).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-------------end template----------------------

}
