package egu.uga.cs.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class JoinOrFormGroupPage extends AppCompatActivity {

    Button fgbutton;
    Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_form_group_page);

        fgbutton = (Button) findViewById(R.id.creategroup);
        joinButton = (Button) findViewById(R.id.joingroup);

        fgbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FormGroup.class));
            }
        }); //end of button

        joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), JoinGroup.class));

            }
        }); //end of button

    }
}