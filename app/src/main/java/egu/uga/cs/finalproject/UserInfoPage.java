package egu.uga.cs.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserInfoPage extends AppCompatActivity {

    private static final String TAG ="ViewDatabase";

    FirebaseAuth fAuth;
    String group;
    String userID;
    String name;
    String email;
    DatabaseReference myRef, myRef2, myRef3;
    FirebaseDatabase mFirebaseDatabase, FD2;


    private ListView mListView;
    private ListView mListView2;
    private DataSnapshot dataSnapshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_page);


        mListView = (ListView) findViewById(R.id.listview1);
        mListView2 = (ListView) findViewById(R.id.listview2);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users");

       /* FD2 = FirebaseDatabase.getInstance();

        myRef2 = FD2.getReference("users").child(userID);*/


        //DatabaseReference newRef = myRef.child("users");
        FirebaseUser user = fAuth.getCurrentUser();


        //myRef = FirebaseDatabase.getInstance().getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /* myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showData2(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    private void showData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {


            email = ds.child("email").getValue(String.class);
            group = ds.child("groupID").getValue(String.class);
            name = ds.child("name").getValue(String.class);

           /* uInfo.setName(ds.getValue(User.class).getName());
            uInfo.setEmail(ds.getValue(User.class).getEmail()); */

            Log.d(TAG, "showData: email: " + email);
            Log.d(TAG, "showData: group: " + group);
            Log.d(TAG, "showData: name: " + name);




            ArrayList<String> array = new ArrayList<>();
            array.add(email);
            array.add(name);
            array.add(group);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListView.setAdapter(adapter);

        }
    }

       /* private void showData2(DataSnapshot dataSnapshot){

            for(DataSnapshot ds: dataSnapshot.getChildren()){

                String member = ds.getValue(String.class);

                Log.d(TAG, "showData: member: " + member);

                ArrayList<String> array = new ArrayList<>();
                array.add(member);

                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array);
                mListView2.setAdapter(adapter);

            }



    }*/



}