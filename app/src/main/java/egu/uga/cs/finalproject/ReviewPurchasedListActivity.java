package egu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewPurchasedListActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "MainActivity";

    // displays the purchased grocery items
    private ListView listView;
    private PurchasedListAdapter arrayAdapter;

    // gets item that's selected
    GroceryItemPurchased selectedFromList;


    private Button undoPurchaseButton;
    private Button editPriceButton;
    private Button settleCostButton;
    private List<GroceryItemPurchased> purchasedItemsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_purchased_list);

        Log.d( DEBUG_TAG, "MainActivity.onCreate()" );

        listView = (ListView) findViewById(R.id.purchasedListView);

        // get a Firebase DB instance reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PurchasedGroceries");

        // instantiate list reference
        purchasedItemsList = new ArrayList<GroceryItemPurchased>();

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
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {

                    //GroceryItem groceryItem = postSnapshot.getValue(GroceryItem.class);
                    String currentGrocery = postSnapshot.child("groceryName").getValue(String.class);

                    purchasedItemsList.add(postSnapshot.getValue(GroceryItemPurchased.class));
                    //groceryItemsList.add(postSnapshot.child("groceryName").getValue(String.class));
                    Log.d( DEBUG_TAG, "MainActivity.onCreate(): added: " + postSnapshot.child("groceryName").getValue(String.class));
                }
                Log.d( DEBUG_TAG, "MainActivity.onCreate(): setting recyclerAdapter" );

                // pass the information to the arrayadapter to create the list rows
                arrayAdapter = new PurchasedListAdapter(getApplicationContext(), R.layout.grocery_item_purchased, purchasedItemsList );
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );

        //button changes views to allow user to undo the purchase of an item
        undoPurchaseButton = (Button) findViewById( R.id.undoPurchaseButton );
        undoPurchaseButton.setOnClickListener( new ReviewPurchasedListActivity.undoPurchaseButtonClickListener());

        //button to allow user to edit the price of an item in the list
        editPriceButton = (Button) findViewById( R.id.changePriceButton );
        editPriceButton.setOnClickListener( new ReviewPurchasedListActivity.editPriceButtonClickListener());

        //button that changes views to allow user to settle the cost of the list
        settleCostButton = (Button) findViewById( R.id.settleCostButton );
        settleCostButton.setOnClickListener( new ReviewPurchasedListActivity.settleCostButtonClickListener());

        // sets the selected item from list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedFromList =(GroceryItemPurchased) (listView.getItemAtPosition(myItemInt));
            }

        });
    }

    // helper method places item to groceries database table
    private void sendItemToMainList (String groceryItemName) {
        String groceryName = groceryItemName;

        final GroceryItem groceryItem = new GroceryItem( groceryName);

        // Add a new element (GroceryItem) to the list of job leads in Firebase.
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

    // removes item from list and adds it back to the main list
    private class undoPurchaseButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (selectedFromList == null) {
                Toast.makeText(getApplicationContext(), "Please select an item to remove.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query groceryQuery = ref.child("PurchasedGroceries").orderByChild("groceryName").equalTo(selectedFromList.getGroceryName());
            groceryQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("selected from list ---------------" + selectedFromList );
                    for (DataSnapshot grocerySnapshot: dataSnapshot.getChildren()) {
                        final String itemName = grocerySnapshot.child("groceryName").getValue(String.class);
                        // deletes item from firebase and waits till success to reload updated list
                        grocerySnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                sendItemToMainList(itemName); //sends selected item to main list
                                recreate(); //reloads updated list
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(DEBUG_TAG, "onCancelled", databaseError.toException());
                }
            });
        }
    }

    // edits the price of a chosen item
    private class editPriceButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (selectedFromList == null){
                Toast.makeText(getApplicationContext(), "Please select an item to update the price.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(view.getContext(), UpdatePriceActivity.class);
            intent.putExtra("selectedFromList", selectedFromList.getGroceryName());
            view.getContext().startActivity(intent);

        }
    }


    // opens new view to add a new item
    private class settleCostButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NewGroceryItemActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "MainActivity.onResume()" );
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "MainActivity.onPause()" );
        super.onPause();
    }

    // These activity callback methods are not needed and are for edational purposes only
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "MainActivity.onStart()" );
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "MainActivity.onStop()" );
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "MainActivity.onDestroy()" );
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "MainActivity.onRestart()" );
        super.onRestart();
    }
}
