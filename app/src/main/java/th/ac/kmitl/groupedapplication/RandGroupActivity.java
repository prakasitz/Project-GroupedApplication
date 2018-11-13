package th.ac.kmitl.groupedapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

public class RandGroupActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvQty;
    private Integer radioValue = 2;
    private RadioButton g2,g3,g4,g5,g6;
    private Button btn_setGroup;
    ArrayList<String> userInClass = new ArrayList<String>();
    ArrayList<String> modQty = new ArrayList<String>();
    Map<String,Object> GroupInfoUpdate = new HashMap<>();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference myRef;

    private int countGroup;
    private int countUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randgroup);

        tvQty = findViewById(R.id.tvQty);
        g2 = findViewById(R.id.radio_g2);
        g3 = findViewById(R.id.radio_g3);
        g4 = findViewById(R.id.radio_g4);
        g5 = findViewById(R.id.radio_g5);
        g6 = findViewById(R.id.radio_g6);
        btn_setGroup = findViewById(R.id.btn_setGroup);

        g2.setOnClickListener(this);
        g3.setOnClickListener(this);
        g4.setOnClickListener(this);
        g5.setOnClickListener(this);
        g6.setOnClickListener(this);
        btn_setGroup.setOnClickListener(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("classrooms");
        mAuth = FirebaseAuth.getInstance();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot userData = dataSnapshot.child("1/"+"member_list");
                Log.wtf("userData", String.valueOf(userData));
                countUser = Integer.parseInt(String.valueOf(userData.getChildrenCount()));
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
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("group_list");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int num = 0;
                    int plus = 1;
                    DataSnapshot groupList = dataSnapshot;
                    countGroup = Integer.parseInt(String.valueOf(groupList.getChildrenCount()));
                    //countUser;
                    //radioValue;
                    Log.wtf("countGroup",  String.valueOf(countGroup));
                    for(int i = 0; i < userInClass.size() ; i++) {
                        GroupInfoUpdate.put((countGroup+plus)+"/member_list/"+(num+1), userInClass.get(i));
                        num++;
                        if(num==radioValue){
                            num = 0;
                            GroupInfoUpdate.put((countGroup+plus)+"/proj_id", "");
                            plus++;
                        }
                    }
                    if(num!=0) {
                        GroupInfoUpdate.put((countGroup + plus) + "/proj_id", "");
                    }
                    myRef.updateChildren(GroupInfoUpdate);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("errorConnect", String.valueOf(databaseError));
                }
            });

        }
    }
}
