package com.bandit.mshop.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bandit.mshop.R;

import com.mikhaellopez.circularimageview.CircularImageView;

public class ItemListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] name;
    private final Integer[] price;
    private final Bitmap[] image;

    public ItemListAdapter(Activity context, ItemAdapterModel item){
        super(context, R.layout.listview_item_row , item.getId());
        this.context = context;
        this.name = item.getName();
        this.price = item.getPrice();
        this.image = item.getImage();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_item_row, null,true);

        TextView textViewName = rowView.findViewById(R.id.textViewName);
        TextView textViewPrice = rowView.findViewById(R.id.textViewPrice);
        CircularImageView circleImageView = rowView.findViewById(R.id.circleImageViewItem);

        textViewName.setText(this.name[position]);
        textViewPrice.setText("Цена: " + this.price[position] + " руб.");
        circleImageView.setImageBitmap(image[position]);

        return rowView;
    };
}
