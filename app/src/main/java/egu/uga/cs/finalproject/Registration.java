package egu.uga.cs.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    EditText etemail;
    EditText etpassword;
    EditText etname;
    Button button;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etemail = (EditText) findViewById(R.id.email);
        etpassword = (EditText) findViewById(R.id.password);
        etname = (EditText) findViewById(R.id.editName);

        button = (Button) findViewById(R.id.registerbutton);
        fAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String fullname = etname.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();

                /*if (!validateName() || !validateEmail() || !validatePassword()) {
                    return;
                }*/

                if (TextUtils.isEmpty(email)) {
                    etemail.setError("email is required");
                    return;

                }

                if (TextUtils.isEmpty(password)) {
                    etpassword.setError("email is required");
                    return;

                }

                if (password.length() < 6) {
                    etpassword.setError("Password Must be >= 6 characters");

                }

                if (TextUtils.isEmpty(fullname)) {
                    etname.setError("email is required");
                    return;

                }


                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registration.this, "User created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Registration.this, "Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();


                        }
                    }
                });


            }


        });

    /*private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!validateName() || !validateEmail() || !validatePassword()) {
                return;
            }

            String fullname = etemail.getText().toString();
            String email = etpassword.getText().toString();
            String password = etname.getText().toString();

        }
    }



    private Boolean validateName(){

        String value = etname.getText().toString();

        if (value.isEmpty()) {
            etname.setError("Field cannot be empty");
            return false;
        } else {
            etname.setError(null);
            return true;
        }
    }

    private Boolean validateEmail () {

        String value = etname.getText().toString();
        String emailPattern = "a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (value.isEmpty()) {
            etname.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailPattern)) {
            etname.setError("Invalid email address.");
            return false;
        } else {
            etname.setError(null);
            return true;
        }
    }


    private Boolean validatePassword () {

        String value = etname.getText().toString();
        String passwordVal = "^" + "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (value.isEmpty()) {
            etname.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(passwordVal)) {
            etname.setError("Password is too weak.");
            return false;
        } else {
            etname.setError(null);
            return true;
        }
    }

*/


    }
}

