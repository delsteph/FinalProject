package egu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JoinGroup extends AppCompatActivity {

    FirebaseAuth fAuth;
    String groupID;
    TextView groupCode;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        groupCode = (TextView) findViewById(R.id.groupName);


        button = (Button) findViewById(R.id.button2);
        fAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                groupID = groupCode.getText().toString();

                if (TextUtils.isEmpty(groupID)) {
                    groupCode.setError("Group ID is required");
                    return; //dont procede further

                } else {
                    joinGroup(groupID);
                }
            }
        }); //end of button
    }


    private void joinGroup(final String groupID) {

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");
        ref1.child(groupID).child("Participants").push()
                .setValue(fAuth.getCurrentUser().getEmail())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(JoinGroup.this, "User has been added to group", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                });


    }

}

