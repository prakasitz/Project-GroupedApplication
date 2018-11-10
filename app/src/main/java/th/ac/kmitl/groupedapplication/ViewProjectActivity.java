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

import java.util.ArrayList;

import th.ac.kmitl.groupedapplication.adapter.ProjectItemClickListener;
import th.ac.kmitl.groupedapplication.adapter.ProjectListAdapter;
import th.ac.kmitl.groupedapplication.controller.setNavHeader;
import th.ac.kmitl.groupedapplication.model.Project;

public class ViewProjectActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,ProjectItemClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    //-------------View-----------------
    private TextView textEmail;
    private TextView textFullName;
    private RecyclerView recyclerView;
    //------------String-----------------
    private String uid;
    private String ustatus;
    private String classid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ///--------------recycleview--------------------------
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Project);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ////-------------------coding----------------------////
        Log.i("test","hello");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //------------setVisibility---------------
        findViewById(R.id.recyclerView_Project).setVisibility(View.VISIBLE);
        findViewById(R.id.inc_classroom_menu).setVisibility(View.GONE);
        findViewById(R.id.inc_class).setVisibility(View.GONE);
        findViewById(R.id.inc_profile).setVisibility(View.GONE);
        //----------------------------------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        ustatus = getI.getStringExtra("ustatus");
        classid = getI.getStringExtra("classid");
        //----------------------end--------------------------------
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);

        getDataResults();
    }

    public void getDataResults(){
        final ArrayList<Project> projectArrayList = new ArrayList<Project>();
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("project_list");
        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String ProjectID = dataSnapshot.getKey();
                Log.d("getkey",ProjectID);
                String ProjectName = dataSnapshot.child("proj_name").getValue().toString();
                Project project = new Project(ProjectID,ProjectName);
                projectArrayList.add(project);
                getAllProject(projectArrayList);

                Log.d("get", project.getProject_name());
                Log.d("size",String.valueOf(projectArrayList.size()));
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
        Log.d("ProjectAdapters",String.valueOf(Projectlist.size()));
        ProjectListAdapter ProjectAdapter = new ProjectListAdapter(projects, ViewProjectActivity.this);
        Log.d("ProjectAdapter",String.valueOf(ProjectAdapter.getItemCount()));
        recyclerView.setAdapter(ProjectAdapter);
    }

    @Override
    public void onProjectItemClick(String projID){
        Toast.makeText(this, "ProjectID : "+ projID, Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, ProjectDetailActivity.class);
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        i.putExtra("classid", ProjectID);
        i.putExtra("uid", uid);
        i.putExtra("ustatus", ustatus); // is 0 or 1
        startActivity(i);*/
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
        textEmail = findViewById(R.id.textEmail);
        textFullName = findViewById(R.id.textFullName);
        new setNavHeader(uid, textEmail, textFullName);
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
            Intent i = new Intent(ViewProjectActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_classroom) {

        } else if (id == R.id.nav_classcreate) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) { //---logout
            mAuth.signOut();
            Toast.makeText(ViewProjectActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewProjectActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-------------end template----------------------

}
