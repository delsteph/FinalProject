package egu.uga.cs.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;


public class Login extends AppCompatActivity {

    Button loginButton;
    Button registerButton;
    FirebaseAuth fAuth;
    private static final int RC_SIGN_IN = 123;
    private static final String DEBUG_TAG = "Login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginbtn);
        registerButton = (Button) findViewById(R.id.registerbtn);

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser()!=null){

            fAuth.signOut();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                );


                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN);

            }
        }); //end of button

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Registration.class));


            }
        }); //end of button




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d( DEBUG_TAG, "JobLead: MainActivity.onActivityResult()" );

        // check if it is a sign in activity result
        if( requestCode == RC_SIGN_IN ) {
            IdpResponse response = IdpResponse.fromResultIntent( data );

            if( resultCode == RESULT_OK ) {

                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Log.i( "FireBase Test", "Signed in as: " + user.getEmail() );

                // after a successful sign in, start the main activity

                Intent intent = new Intent( this, MainActivity.class );
                startActivity( intent );

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in, e.g., by using the back button
                Toast.makeText( getApplicationContext(),
                        "Sign in failed",
                        Toast.LENGTH_SHORT).show();            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }
}