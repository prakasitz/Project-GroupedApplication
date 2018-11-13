package th.ac.kmitl.groupedapplication;

import android.content.Intent;
import android.icu.text.StringPrepParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import th.ac.kmitl.groupedapplication.controller.setNavHeader;

public class ClassroomShowIDActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ChildEventListener childEventListener;
    private Query QuerybyUID;

    private TextView tvEmail;
    private TextView tvFullName;
    private TextView tvSubject;
    private TextView tvClassid;

    private String uid;
    private String ustatus;
    private String classid;
    private String classname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //--------------mthu---------------------
        mAuth = FirebaseAuth.getInstance();
        //------------setVisibility---------------
        findViewById(R.id.inc_classroom_show_id).setVisibility(View.VISIBLE);
        //----------------view------------------------
        tvSubject = findViewById(R.id.title_subject_in_show_id);
        tvClassid = findViewById(R.id.title_class_id);
        //-------รับค่าจากหน้า classroommemnu เท่านั้น------------
        Intent getI = getIntent();
        uid = getI.getStringExtra("uid");
        classid = getI.getStringExtra("classid");
        ustatus = getI.getStringExtra("ustatus");
        classname = getI.getStringExtra("classname");
        //--------------setText-------------------
        tvSubject.setText(classname);
        Log.e("uidLength",uid);
        Log.e("classidLength",classid);

        try {
            if(classid.length() <= 6) {
                tvClassid.setText(classid);
            } else if(classid.length() <= 20) {
                tvClassid.setText(classid.substring(15));
            }
        } catch (Exception e) {
            Toast.makeText(ClassroomShowIDActivity.this,"classid error!",Toast.LENGTH_SHORT).show();
            Log.e("classidError",e.toString());
        }
        //---------------drawer menu bar---------------------
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //-------------create nav--------------------------------
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_classroom);
        Log.e("test","test");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener != null) {
            myRef.removeEventListener(childEventListener);
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

        if (id == R.id.action_createClassroom) {
            Intent i = new Intent(ClassroomShowIDActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_classroom_show_id).setVisibility(View.GONE);
            finish();
            return true;
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
            Intent i = new Intent(ClassroomShowIDActivity.this, ProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_classroom_show_id).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_classroom) {
            finish();
        } else if (id == R.id.nav_classcreate) {
            Intent i = new Intent(ClassroomShowIDActivity.this, ClassroomCreateActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
            findViewById(R.id.inc_classroom_show_id).setVisibility(View.GONE);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(ClassroomShowIDActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(ClassroomShowIDActivity.this, "ออกจากระบบแล้ว!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.inc_classroom_show_id).setVisibility(View.GONE);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
