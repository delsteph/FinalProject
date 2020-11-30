package egu.uga.cs.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class Registration extends AppCompatActivity {


    EditText etemail, etpassword, etname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etname=findViewById(R.id.editName);
        etemail=findViewById(R.id.email);
        etpassword=findViewById(R.id.confirmpassword);

    }
}