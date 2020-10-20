package com.bandit.mshop.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bandit.mshop.R;

public class PriceListAdapter extends ArrayAdapter {
    private final Activity context;
    private final Integer[] idItem;
    private final String[] categoryName;
    private final String[] itemName;
    private final Integer[] itemPrice;

    public PriceListAdapter(Activity context, PriceAdapterModel price){
        super(context, R.layout.listview_category_row , price.getItemId());
        this.context = context;
        this.idItem = price.getItemId();
        this.categoryName = price.getCategoryName();
        this.itemName = price.getItemName();
        this.itemPrice = price.getItemPrice();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_price_row, null,true);

        TextView textViewCategory = rowView.findViewById(R.id.textViewCategoryPrice);
        TextView textViewItem = rowView.findViewById(R.id.textViewItemPrice);
        TextView textViewPrice = rowView.findViewById(R.id.textViewPricePrice);
        textViewCategory.setText(this.categoryName[position]);
        textViewItem.setText(this.itemName[position]);
        textViewPrice.setText(String.valueOf(this.itemPrice[position]));

        return rowView;
    };
}

