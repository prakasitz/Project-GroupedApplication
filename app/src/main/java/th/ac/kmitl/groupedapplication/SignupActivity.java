package th.ac.kmitl.groupedapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText edID;
    private EditText edName;
    private EditText edLastName;
    private EditText edEmail;
    private EditText edPass;
    private Button btnSignUp;


    private UserSignUpTask mAuthTask;
    private View mProgressView;
    private View mLoginFormView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference myRef;

    private String mID;
    private String mName;
    private String mLName;
    private String mEmail;
    private String mPass;
    private Integer radioValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
        edID = findViewById(R.id.ids);
        edName = findViewById(R.id.name);
        edLastName = findViewById(R.id.lname);
        edEmail = findViewById(R.id.email);
        edPass = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.SignupButton);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mID = edID.getText().toString();
                mName = edName.getText().toString();
                mLName = edLastName.getText().toString();
                mEmail = edEmail.getText().toString();
                mPass = edPass.getText().toString();

                if(mID == null || mName == null || mLName == null || mEmail == null || mPass == null) {
                    Toast.makeText(SignupActivity.this, "มีบางอย่างที่ไม่ได้กรอก!", Toast.LENGTH_SHORT).show();
                } else {
                    showProgress(true);
                    mAuthTask = new SignupActivity.UserSignUpTask(mID, mName, mLName,mEmail,mPass,radioValue);
                    mAuthTask.execute((Void) null);
                }

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String uid = String.valueOf(user.getUid());
                    Log.d("user",uid);
                    myRef = database.getReference();
                    Map<String, Object> usersTable = new HashMap<>();
                    usersTable.put("email", mEmail);
                    usersTable.put("status", radioValue);
                    myRef.child("users/"+uid).updateChildren(usersTable);

                    myRef = database.getReference();
                    Map<String, Object> StudentTable = new HashMap<>();
                    StudentTable.put("std_id", mID);
                    StudentTable.put("std_name", mName);
                    StudentTable.put("std_lname", mLName);
                    StudentTable.put("std_post", "");
                    StudentTable.put("std_status", radioValue);
                    StudentTable.put("uid", uid);

                    myRef.child("student/"+uid).updateChildren(StudentTable);
                    Toast.makeText(SignupActivity.this, "ลงทะเบียนสำเร็จ!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignupActivity.this, ClassroomActivity  .class);
                    intent.putExtra("uid",user.getUid());
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed out
                }

            }
        };

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }// end onCreate

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_student:
                if (checked)
                    radioValue = 0;
                    Log.d("studentCheck","checked");
                    break;
            case R.id.radio_teacher:
                if (checked)
                    radioValue = 1;
                    Log.d("TeacherCheck","checked");
                    break;
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {
        private final String mID;
        private final String mName;
        private final String mLname;
        private final String mEmail;
        private final String mPassword;
        private final int mStatus;
        boolean isSuccessful;
        boolean isComplete;

        UserSignUpTask(String id, String name, String lastname, String email, String password,int statusvalue) {
            mID = id;
            mName = name;
            mLname = lastname;
            mEmail = email;
            mPassword = password;
            mStatus = statusvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAuth = FirebaseAuth.getInstance();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

           mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "มีผู้ใช้ email นี้แล้วหรือรหัสผ่านน้อยกว่า 6 ตัว.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("SignUp-Success","successed!!");
                            }
                        }
                    });

            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                //mAuthTask = null;
                showProgress(false);
                Log.d("testSignUp","success!");
                Log.d("input",mID+", "+mName+", "+mLname+", "+mEmail+", "+mPassword+", "+mStatus);
                //finish();
                //Intent i = new Intent(LoginActivity.this,MainActivity.class);
                //LoginActivity.this.startActivity(i);
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            showProgress(false);
        }
    }
}
