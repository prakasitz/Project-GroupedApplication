package th.ac.kmitl.groupedapplication.controller;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class setNavHeader {
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;

    public static String ustatus;

    public setNavHeader(final String uid, final TextView tvEmail, final TextView tvFullName ) {
        //----------firebase-------------
        Log.e("setNavHeader","ok");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("stNav>DataChang","ok");
                Log.d("LogIn",uid);

                String status_str;

                DataSnapshot usersPath = dataSnapshot.child(uid);
                DataSnapshot usersPathEmail = dataSnapshot.child(uid+"/email");
                DataSnapshot usersPathStatus = dataSnapshot.child(uid+"/level");

                if(usersPathStatus.getValue() != null) {
                    ustatus = String.valueOf(usersPathStatus.getValue());
                    switch (ustatus) {
                        case "1": status_str = " (อาจารย์)";

                            tvEmail.setText(String.valueOf(usersPathEmail.getValue()));
                            tvFullName.setText(String.valueOf(
                                    usersPath.child("fname").getValue() + " " +
                                            usersPath.child("lname").getValue() + status_str ));
                            break;
                        case "0": status_str = " (นักเรียน)";
                            tvEmail.setText(String.valueOf(usersPathEmail.getValue()));
                            tvFullName.setText(String.valueOf(
                                    usersPath.child("fname").getValue() + " " +
                                            usersPath.child("lname").getValue() + status_str ));
                            break;
                        default: break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errorConnect",String.valueOf(databaseError));
            }

        });
    }
}
