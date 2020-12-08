package egu.uga.cs.finalproject;

import android.content.Context;
import android.widget.ArrayAdapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasedListAdapter extends ArrayAdapter<GroceryItemPurchased> {

    private int resourceLayout;
    private Context mContext;

    public PurchasedListAdapter(Context context, int resource, List<GroceryItemPurchased> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        GroceryItemPurchased p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.groceryName);
            TextView tt2 = (TextView) v.findViewById(R.id.price);
            TextView tt3 = (TextView) v.findViewById(R.id.purchaserName);

            if (tt1 != null) {
                tt1.setText(p.getGroceryName());
            }

            if (tt2 != null) {
                tt2.setText("Price: " + p.getPrice());
            }

            if (tt3 != null) {
                tt3.setText("Purchaser Name: " + p.getPurchaserName());
            }
        }

        return v;
    }
}
