package egu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UpdatePriceActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "PurchaseItemActivity";

    private TextView itemToPurchaseTV;
    private EditText priceView;
    private Button updateButton;
    private Button cancelButton;
    private String selectedFromListValue;

    // to get current signed in user
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_price);

        // to get current signed in user
        mAuth = FirebaseAuth.getInstance();
        currentUserString = "user";

        // gets information passed to it from main view
        Intent mIntent = getIntent();
        selectedFromListValue = mIntent.getStringExtra("selectedFromList");

        itemToPurchaseTV = (TextView) findViewById( R.id.itemToUpdateTextView);
        itemToPurchaseTV.setText("You are updating the price of this grocery item: " + selectedFromListValue);

        priceView = (EditText) findViewById( R.id.enterNewPriceView);
        updateButton = (Button) findViewById( R.id.updatePriceButton);
        cancelButton = (Button) findViewById( R.id.cancelUpdateButton);

        // if button is clicked, updates price in the database
        updateButton.setOnClickListener( new UpdatePriceActivity.updateButtonClickListener());
        // returns user to main page (list)
        cancelButton.setOnClickListener( new UpdatePriceActivity.cancelButtonClickListener());
    }

    private class cancelButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewPurchasedListActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class updateButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final String groceryPrice = priceView.getText().toString();
            if (groceryPrice == null) {
                Toast.makeText(getApplicationContext(), "Please write the updated price.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("PurchasedGroceries");

            myRef.child(selectedFromListValue).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getRef().child("price").setValue(groceryPrice);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User", databaseError.getMessage());
                }
            });

            // switches to the purchased list view
            Intent intent = new Intent(v.getContext(), ReviewPurchasedListActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    //removes the purchased item from the grocery list
    private void removePurchasedItem() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query groceryQuery = ref.child("groceries").orderByChild("groceryName").equalTo(selectedFromListValue);

        groceryQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("selected from list ---------------" + selectedFromListValue );
                for (DataSnapshot grocerySnapshot: dataSnapshot.getChildren()) {
                    // deletes item from firebase
                    grocerySnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(DEBUG_TAG, "onCancelled", databaseError.toException());
            }
        });
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
