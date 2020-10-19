package com.bandit.mshop.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bandit.mshop.R;

public class CartListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] name;
    private final Integer[] price;
    private final Integer[] amount;

    public CartListAdapter(Activity context, CartAdapterModel cart){
        super(context, R.layout.listview_cart_row , cart.getId());
        this.context = context;
        this.name = cart.getName();
        this.price = cart.getPrice();
        this.amount = cart.getAmount();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_cart_row, null,true);

        TextView textViewName = rowView.findViewById(R.id.textViewNameCart);
        TextView textViewPrice = rowView.findViewById(R.id.textViewPriceCart);
        TextView textViewAmount = rowView.findViewById(R.id.textViewAmountCart);

        textViewName.setText(this.name[position]);
        textViewPrice.setText(String.valueOf(this.price[position]));
        textViewAmount.setText(String.valueOf(this.amount[position]));

        return rowView;
    };
}