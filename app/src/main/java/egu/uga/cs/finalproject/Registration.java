package egu.uga.cs.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    EditText etemail;
    EditText etpassword;
    EditText etname;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etemail = (EditText) findViewById(R.id.email);
        etpassword = (EditText) findViewById(R.id.password);
        etname = (EditText) findViewById(R.id.editName);

        button = (Button) findViewById(R.id.registerbutton);


        button.setOnClickListener(new RegisterButtonClickListener());

    }

    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(),SubmitRegisterInfo.class);/**will make submitregister class*/
            view.getContext().startActivity(intent);
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


        public void registerUser (View view){

            if (!validateName() || !validateEmail() || !validatePassword()) {
                return;
            }

            String fullname = etemail.getText().toString();
            String email = etpassword.getText().toString();
            String password = etname.getText().toString();

        }

}