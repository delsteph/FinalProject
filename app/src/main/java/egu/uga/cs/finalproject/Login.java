package egu.uga.cs.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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


public class Login extends AppCompatActivity {

    Button loginButton = (Button) findViewById(R.id.loginbtn);
    Button registerButton = (Button) findViewById(R.id.registerbtn);
    EditText emailInput = (EditText) findViewById(R.id.emailfield);
    EditText passwordInput = (EditText) findViewById(R.id.passwordfield);
    final FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_form_group_page);

        if(fAuth.getCurrentUser()!=null){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email is required");
                    return;

                }

                if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password is required");
                    return;

                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }); //end of button

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Registration.class));

            }
        }); //end of button
    }
}