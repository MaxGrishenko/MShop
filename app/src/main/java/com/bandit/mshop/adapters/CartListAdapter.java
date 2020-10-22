package com.bandit.mshop.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bandit.mshop.R;

public class CartListAdapter extends ArrayAdapter {
    private final Activity context;
    private final Integer[] id;
    private final String[] name;
    private final Integer[] price;
    private final Integer[] discount;
    private final Integer[] amount;

    public CartListAdapter(Activity context, CartAdapterModel cart){
        super(context, R.layout.listview_cart_row , cart.getId());
        this.context = context;
        this.id = cart.getId();
        this.name = cart.getName();
        this.price = cart.getPrice();
        this.discount = cart.getDiscount();
        this.amount = cart.getAmount();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_cart_row, null,true);

        TextView textViewName = rowView.findViewById(R.id.textViewNameCart);
        TextView textViewPrice = rowView.findViewById(R.id.textViewPriceCart);
        TextView textViewAmount = rowView.findViewById(R.id.textViewAmountCart);
        TextView textViewTotal = rowView.findViewById(R.id.textViewTotalCart);
        ImageButton buttonDelete = rowView.findViewById(R.id.buttonRemoveCart);
        ImageButton buttonAdd = rowView.findViewById(R.id.buttonAddCart);
        ImageButton buttonSell = rowView.findViewById(R.id.buttonSubCart);
        LinearLayout linearLayoutCart = rowView.findViewById(R.id.linearLayoutCart);

        textViewName.setText(this.name[position]);
        textViewPrice.setText(String.valueOf(this.price[position]));
        textViewAmount.setText(String.valueOf(this.amount[position]));
        textViewTotal.setText(String.valueOf(this.price[position] * this.amount[position]));
        buttonDelete.setTag(this.id[position]);
        buttonSell.setTag(this.id[position]);
        buttonAdd.setTag(this.id[position]);

        if (this.discount[position] != 0){
            linearLayoutCart.setBackgroundResource(R.color.discount);
            buttonDelete.setBackgroundResource(R.color.discount);
            buttonAdd.setBackgroundResource(R.color.discount);
            buttonSell.setBackgroundResource(R.color.discount);
            textViewPrice.setText(String.valueOf(this.price[position] / this.discount[position]));
            textViewTotal.setText(String.valueOf(this.price[position] / this.discount[position] * this.amount[position]));
        }

        return rowView;
    };
}