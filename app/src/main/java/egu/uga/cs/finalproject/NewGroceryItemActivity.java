package egu.uga.cs.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewGroceryItemActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "NewGroceryItemActivity";

    private EditText groceryNameView;
    //private EditText priceView;
    //private EditText quantityView;
    private Button saveButton;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_grocery_item);

        groceryNameView = (EditText) findViewById( R.id.editTextGroceryName );
        //priceView = (EditText) findViewById( R.id.editNumberPrice );
        //quantityView = (EditText) findViewById( R.id.editNumberDecimalQuantity );
        saveButton = (Button) findViewById( R.id.addItemButton );
        doneButton = (Button) findViewById( R.id.doneButton );

        // if button is clicked, adds item to database
        saveButton.setOnClickListener( new ButtonClickListener());
        // returns user to main page (list)
        doneButton.setOnClickListener( new doneButtonClickListener());
    }

    private class doneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String groceryName = groceryNameView.getText().toString();
            //String price = priceView.getText().toString();
            //String quantity = quantityView.getText().toString();
            final GroceryItem groceryItem = new GroceryItem( groceryName);

            // Add a new element (JobLead) to the list of job leads in Firebase.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("groceries");

            // First, a call to push() appends a new node to the existing list (one is created
            // if this is done for the first time).  Then, we set the value in the newly created
            // list node to store the new job lead.
            // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
            // the previous apps to maintain job leads.
            myRef.push().setValue( groceryItem )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            Toast.makeText(getApplicationContext(), "Item added to grocery list:  " + groceryItem,
                                    Toast.LENGTH_SHORT).show();

                            // Clear the EditTexts for next use.
                            groceryNameView.setText("");
                            //priceView.setText("");
                            //quantityView.setText("");
                        }
                    })
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText( getApplicationContext(), "Failed to create a Job lead for " + groceryItem,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "NewJobLeadActivity.onResume()" );
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "NewJobLeadActivity.onPause()" );
        super.onPause();
    }

    // The following activity callback methods are not needed and are for educational purposes only
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "NewJobLeadActivity.onStart()" );
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "NewJobLeadActivity.onStop()" );
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "NewJobLeadActivity.onDestroy()" );
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "NewJobLeadActivity.onRestart()" );
        super.onRestart();
    }
}
