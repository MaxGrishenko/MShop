package com.bandit.mshop.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bandit.mshop.R;

import com.mikhaellopez.circularimageview.CircularImageView;

public class ItemListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] name;
    private final Integer[] price;
    private final Bitmap[] image;
    private final Integer[] discount;

    public ItemListAdapter(Activity context, ItemAdapterModel item){
        super(context, R.layout.listview_item_row , item.getId());
        this.context = context;
        this.name = item.getName();
        this.price = item.getPrice();
        this.image = item.getImage();
        this.discount = item.getDiscount();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_item_row, null,true);

        TextView textViewName = rowView.findViewById(R.id.textViewName);
        TextView textViewPrice = rowView.findViewById(R.id.textViewPrice);
        CircularImageView circleImageView = rowView.findViewById(R.id.circleImageViewItem);
        LinearLayout linearLayoutItem = rowView.findViewById(R.id.linearLayoutItem);

        textViewName.setText(this.name[position]);
        textViewPrice.setText("Цена: " + this.price[position] + " руб.");
        circleImageView.setImageBitmap(image[position]);

        if (this.discount[position] != 0){
            linearLayoutItem.setBackgroundResource(R.color.discount);
            textViewPrice.setText("Цена: " + this.price[position] / this.discount[position] + " руб.");
        }

        return rowView;
    };
}
