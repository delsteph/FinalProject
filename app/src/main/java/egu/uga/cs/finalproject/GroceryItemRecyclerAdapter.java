package egu.uga.cs.finalproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class GroceryItemRecyclerAdapter extends RecyclerView.Adapter<GroceryItemRecyclerAdapter.GroceryItemHolder> {
    public static final String DEBUG_TAG = "GroceryItemRecycler";

    private List<GroceryItem> groceryItemList;

    public GroceryItemRecyclerAdapter( List<GroceryItem> groceryItemList ) {
        this.groceryItemList = groceryItemList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class GroceryItemHolder extends RecyclerView.ViewHolder {

        TextView groceryName;
        TextView quantity;

        public GroceryItemHolder(View itemView ) {
            super(itemView);

            groceryName = (TextView) itemView.findViewById( R.id.groceryName );
            quantity = (TextView) itemView.findViewById( R.id.groceryQuantity );
        }
    }

    @Override
    public GroceryItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.grocery_item, parent, false );
        return new GroceryItemHolder( view );
    }

    // This method fills in the values of the Views to show a GroceryItem
    @Override
    public void onBindViewHolder( GroceryItemHolder holder, int position ) {
        GroceryItem groceryItem = groceryItemList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + groceryItem );

        holder.groceryName.setText( groceryItem.getGroceryName());
        holder.quantity.setText( groceryItem.getQuantity() );

    }

    @Override
    public int getItemCount() {
        return groceryItemList.size();
    }
}
