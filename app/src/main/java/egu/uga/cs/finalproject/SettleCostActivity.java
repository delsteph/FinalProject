package egu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SettleCostActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "SettleCostActivity";

    //variables
    private ArrayList<Double> userOnePurchases;
    private String buyer1;
    private Double userOneSum = 0.0;
    private ArrayList<Double> userTwoPurchases;
    private String buyer2;
    private Double userTwoSum = 0.0;
    private ArrayList<Double> userThreeSum;
    private String buyer3 = "";

    // saves the emails of the users
    private ArrayList<String> array = new ArrayList<>();
    private String currentUserGroupID;


    private Button cancelButton;
    private Button finalizeButton;
    private TextView displayInfo;
    private TextView displayInfo2;
    private List<GroceryItemPurchased> purchasedItemsList;

    // to get current signed in user
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_cost);

        displayInfo = (TextView) findViewById( R.id.displayCostInfo );
        displayInfo2 = (TextView) findViewById( R.id.displayCostInfo2 );

        // to get current signed in user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserEmail = currentUser.getEmail();

        //gets the group id of the current user
        getCurrentUserID();
        //getUsersWithID(currentUserGroupID);


        // get a Firebase DB instance reference
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("PurchasedGroceries");

        // instantiate list reference
        purchasedItemsList = new ArrayList<GroceryItemPurchased>();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // get a Firebase DB instance reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("PurchasedGroceries");
                // Set up a listener (event handler) to receive a value for the database reference, but only one time.
                // This type of listener is called by Firebase once by immediately executing its onDataChange method.
                // We can use this listener to retrieve the current list of GroceryItems.
                // Other types of Firebase listeners may be set to listen for any and every change in the database
                // i.e., receive notifications about changes in the data in real time (hence the name, Realtime database).
                // This listener will be invoked asynchronously, as no need for an AsyncTask, as in the previous apps
                // to maintain groceries.
                myRef.addListenerForSingleValueEvent( new ValueEventListener() {

                    @Override
                    public void onDataChange( DataSnapshot snapshot ) {
                        System.out.println("print this inisde ----------");
                        buyer1 = array.get(0);
                        System.out.println("print this inisde ----------" + buyer1);
                        buyer2 = array.get(1);
                        System.out.println("print this inisde ----------" + buyer2);
                        // Once we have a DataSnapshot object, knowing that this is a list,
                        // we need to iterate over the elements and place them on a List.
                        for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                            System.out.println("print this inisde here now ----------");
                            // retrieve name of purchaser
                            String purchaser = postSnapshot.child("purchaserName").getValue(String.class);

                            if (purchaser.equals(buyer1)) {
                                String price = postSnapshot.child("price").getValue(String.class);
                                double priceDouble = Double.parseDouble(price);
                                System.out.println("print this price ----------" + priceDouble);
                                userOneSum = userOneSum + priceDouble;
                                //userOnePurchases.add(priceDouble);
                            } else if (purchaser.equals(buyer2)) {
                                String price = postSnapshot.child("price").getValue(String.class);
                                double priceDouble = Double.parseDouble(price);
                                System.out.println("print this price ----------" + priceDouble);
                                userTwoSum = userTwoSum + priceDouble;
                                //userTwoPurchases.add(priceDouble);
                            }
                        }

                        //userOneSum = sumArray(userOnePurchases);
                        Double userOneOwed = userOneSum / 2.0;

                        //userTwoSum = sumArray(userTwoPurchases);
                        Double userTwoOwed = userTwoSum / 2.0;

                        displayInfo.setText(buyer1 + " is owed " + userOneOwed+ " by each roommate.");
                        displayInfo2.setText(buyer2 + " is owed " + userTwoOwed+ " by each roommate.");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getMessage());
                    }
                } );
            }
        }, 5000);


        // if button is clicked, adds purchased item to purchased list in database
        //purchaseButton.setOnClickListener( new PurchaseItemActivity.purchaseButtonClickListener());
        // returns user to main page (list)
       cancelButton = (Button) findViewById( R.id.cancelSettleButton );
       cancelButton.setOnClickListener( new SettleCostActivity.cancelButtonClickListener());

       finalizeButton = (Button) findViewById( R.id.finalizeSettleButton);
       finalizeButton.setOnClickListener( new SettleCostActivity.finalizeButtonClickListener());

    }

    private class cancelButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewPurchasedListActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    //removes the purchased item from the grocery list
    private class finalizeButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("PurchasedGroceries");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot grocerySnapshot : dataSnapshot.getChildren()) {
                        // deletes all the children under "PurchasedGroceries"

                        grocerySnapshot.getRef().removeValue();
                    }
                    Toast.makeText(getApplicationContext(), "Purchased Groceries List has been cleared. You settled the cost.",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(DEBUG_TAG, "onCancelled", databaseError.toException());
                }
            });
        }
    }


    private void getCurrentUserID() {

        // gets the group id
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        Query userQuery1 = ref1.child("users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        userQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    currentUserGroupID = ds.child("groupID").getValue(String.class);

                    System.out.println("-------current user groupc id=====" + currentUserGroupID);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            getUsersWithID(currentUserGroupID);
                        }
                    }, 1000);
                    //getUsersWithID(currentUserGroupID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

       // return currentUserGroupID;
    }

    private void getUsersWithID(String groupID) {
        // gets the users in the group
        System.out.println("-------cinside getusers with id=====" + groupID);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query userQuery = ref.child("users").orderByChild("groupID").equalTo(groupID);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String email = ds.child("email").getValue(String.class);
                    System.out.println("-------current user email=====" + email);

                    array.add(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    private class purchaseButtonClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            //String price = priceView.getText().toString();
//            //String quantity = quantityView.getText().toString();
//            final GroceryItemPurchased groceryItemPurchased =
//                    new GroceryItemPurchased(selectedFromListValue, groceryPrice, currentUserEmail);
//
//            // Add a new element (JobLead) to the list of job leads in Firebase.
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("PurchasedGroceries");
//
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//            //Query groceryQuery = ref.child("PurchasedGroceries").orderByChild("groceryName").equalTo(selectedFromListValue);
//            Query groceryQuery = ref.child("PurchasedGroceries").orderByChild("price").equalTo(priceFromListValue);
//            System.out.println("selected query ---------------" + groceryQuery );
//            groceryQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    System.out.println("selected from list ---------------" + selectedFromListValue );
//                    for (DataSnapshot grocerySnapshot: dataSnapshot.getChildren()) {
//                        //final String itemName = grocerySnapshot.child("groceryName").getValue(String.class);
//                        // updates item price in firebase and waits till success to reload updated list
//                        grocerySnapshot.getRef().child("price").setValue(groceryPrice).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                recreate(); //reloads updated list
//                                // switches to the purchased list view
//                                Intent intent = new Intent(v.getContext(), ReviewPurchasedListActivity.class);
//                                v.getContext().startActivity(intent);
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.e(DEBUG_TAG, "onCancelled", databaseError.toException());
//                }
//            });
//        }
//    }



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
