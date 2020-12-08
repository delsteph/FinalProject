package egu.uga.cs.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "MainActivity";

    // displays the grocery items
    private ListView lv;
    ArrayAdapter<String> arrayAdapter;
    //ArrayAdapter<GroceryItem> arrayAdapter;
    // gets item that's selected
    String selectedFromList;

    //private RecyclerView recyclerView;
    //private RecyclerView.LayoutManager layoutManager;
    //private RecyclerView.Adapter recyclerAdapter;

    private Button addItemButton;
    private Button removeItemButton;
    private Button purchaseItemButton;
    private Button viewPurchasedList;

    //private List<GroceryItem> groceryItemsList;
    private List<String> groceryItemsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d( DEBUG_TAG, "MainActivity.onCreate()" );

        //recyclerView = (RecyclerView) findViewById( R.id.recyclerView );

        lv = (ListView) findViewById(R.id.purchasedListView);

        // use a linear layout manager for the recycler view
        //layoutManager = new LinearLayoutManager(this );
        //recyclerView.setLayoutManager( layoutManager );

        // get a Firebase DB instance reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("groceries");

        //groceryItemsList = new ArrayList<GroceryItem>();
        groceryItemsList = new ArrayList<>();

        // Set up a listener (event handler) to receive a value for the database reference, but only one time.
        // This type of listener is called by Firebase once by immediately executing its onDataChange method.
        // We can use this listener to retrieve the current list of GroceryItems.
        // Other types of Firebase listeners may be set to listen for any and every change in the database
        // i.e., receive notifications about changes in the data in real time (hence the name, Realtime database).
        // This listener will be invoked asynchronously, as no need for an AsyncTask, as in the previous apps
        // to maintain job leads.
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // System.out.println("in the first method ____________----------");
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    //GroceryItem groceryItem = postSnapshot.getValue(GroceryItem.class);

                    String currentGrocery = postSnapshot.child("groceryName").getValue(String.class);

                    //groceryItemsList.add(postSnapshot.getValue(GroceryItem.class));
                    groceryItemsList.add(postSnapshot.child("groceryName").getValue(String.class));
                    Log.d( DEBUG_TAG, "MainActivity.onCreate(): added: " + postSnapshot.child("groceryName").getValue(String.class));
                }
                Log.d( DEBUG_TAG, "MainActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a GroceryItemRecyclerAdapter to populate a ReceyclerView to display the job leads.
                //recyclerAdapter = new GroceryItemRecyclerAdapter( groceryItemsList );
                //recyclerView.setAdapter( recyclerAdapter );

                arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, groceryItemsList );

                lv.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );

        //button changes views to allow user to add new item to list
        addItemButton = (Button) findViewById( R.id.button3 );
        addItemButton.setOnClickListener( new ButtonClickListener());

        //button to allow user to remove item from list
        removeItemButton = (Button) findViewById( R.id.removeItem );
        removeItemButton.setOnClickListener( new removeItemButtonClickListener());

        //button that changes views to allow user to purchase item
        purchaseItemButton = (Button) findViewById( R.id.purchaseItemMainButton );
        purchaseItemButton.setOnClickListener( new purchaseButtonClickListener());

        //button that opens the purchsed list view
        viewPurchasedList = (Button) findViewById( R.id.purchasedListButton );
        viewPurchasedList.setOnClickListener( new purchasedListButtonClickListener());

        Button profile = (Button)findViewById(R.id.userProfile);
        profile.setOnClickListener( new profileButtonClickListener());


        // sets the selected item from list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
            }

        });
    }

    private class purchasedListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), ReviewPurchasedListActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class profileButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), UserInfoPage.class);
            view.getContext().startActivity(intent);
        }
    }

    // removes item from list
    private class removeItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (selectedFromList == null) {
                Toast.makeText(getApplicationContext(), "Please select an item to delete.",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            System.out.println("selected from list ===============" + selectedFromList );

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query groceryQuery = ref.child("groceries").orderByChild("groceryName").equalTo(selectedFromList);

            groceryQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("selected from list ---------------" + selectedFromList );
                    for (DataSnapshot grocerySnapshot: dataSnapshot.getChildren()) {
                        // deletes item from firebase and waits till success to reload updated list
                        grocerySnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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

    // opens new view to add a new item
    private class purchaseButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (selectedFromList == null){
                Toast.makeText(getApplicationContext(), "Please select an item to purchase.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(view.getContext(), PurchaseItemActivity.class);
            intent.putExtra("selectedFromList", selectedFromList);
            view.getContext().startActivity(intent);
        }
    }


    // opens new view to add a new item
    private class ButtonClickListener implements View.OnClickListener {
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