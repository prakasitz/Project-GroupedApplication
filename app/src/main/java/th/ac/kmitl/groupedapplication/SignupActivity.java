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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
    private DatabaseReference myRef;

    private String mID;
    private String mName;
    private String mLName;
    private String mEmail;
    private String mPass;
    private Integer radioValue = 0;

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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        edPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String uid = String.valueOf(user.getUid());
                    Log.d("user",uid);
                    myRef = database.getReference();
                    Map<String, Object> usersTable = new HashMap<>();
                    usersTable.put("id", mID);
                    usersTable.put("email", mEmail);
                    usersTable.put("fname", mName);
                    usersTable.put("lname", mLName);
                    usersTable.put("post", "");
                    usersTable.put("level", radioValue);
                    myRef.child("users/"+uid).updateChildren(usersTable);
                    showProgress(false);
                    Intent intent = new Intent(SignupActivity.this, ClassroomActivity.class);
                    intent.putExtra("uid",user.getUid());
                    intent.putExtra("ustatus",radioValue.toString());
                    startActivity(intent);
                    finish();
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

    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edID.setError(null);
        edEmail.setError(null);
        edName.setError(null);
        edLastName.setError(null);
        edPass.setError(null);

        // Store values at the time of the login attempt.
        String id = edID.getText().toString();
        String name = edName.getText().toString();
        String lastname = edLastName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid id
        if (TextUtils.isEmpty(id)) {
            edID.setError(getString(R.string.error_field_required));
            focusView = edID;
            cancel = true;
        }

        // Check for a valid id
        if (TextUtils.isEmpty(name)) {
            edName.setError(getString(R.string.error_field_required));
            focusView = edName;
            cancel = true;
        }

        // Check for a valid id
        if (TextUtils.isEmpty(lastname)) {
            edLastName.setError(getString(R.string.error_field_required));
            focusView = edLastName;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edEmail.setError(getString(R.string.error_field_required));
            focusView = edEmail;
            cancel = true;
        }  else if (!isEmailValid(email)) {
            edEmail.setError(getString(R.string.error_invalid_email));
            focusView = edEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            edPass.setError(getString(R.string.error_field_required));
            focusView = edPass;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edPass.setError(getString(R.string.error_invalid_password));
            focusView = edPass;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserSignUpTask(id, name, lastname, email, password, radioValue);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {
        boolean isSuccessful;
        boolean isComplete;

        UserSignUpTask(String id, String name, String lastname, String email, String password,int statusvalue) {
            mID = id;
            mName = name;
            mLName = lastname;
            mEmail = email;
            mPass = password;
            radioValue = statusvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAuth = FirebaseAuth.getInstance();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
           mAuth.createUserWithEmailAndPassword(mEmail, mPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "มีผู้ใช้อีเมลนี้แล้ว", Toast.LENGTH_SHORT).show();
                            } else {
                                isSuccessful = task.isSuccessful();
                                isComplete = task.isSuccessful();
                                Toast.makeText(SignupActivity.this, "ลงทะเบียนสำเร็จ!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }

            return isComplete && isSuccessful;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if(!success) {
                showProgress(false);
            } else {
                Log.d("testSignUp","success!");
                Log.d("input",mID+", "+mName+", "+mLName+", "+mEmail+", "+mPass+", "+radioValue);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
