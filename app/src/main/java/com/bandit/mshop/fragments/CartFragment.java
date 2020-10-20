package com.bandit.mshop.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bandit.mshop.R;
import com.bandit.mshop.adapters.CartAdapterModel;
import com.bandit.mshop.adapters.CartListAdapter;
import com.bandit.mshop.database.DBAccess;

/**
 * A simple {@link Fragment} subclass.
 */
public class    CartFragment extends Fragment {
    CartListAdapter cartAdapter;
    ListView listViewCart;
    DBAccess dbAccess;
    Integer[] cartItemId;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        dbAccess = DBAccess.getInstance(getActivity().getApplicationContext());

        dbAccess.open();
        CartAdapterModel cartAdapterModel = dbAccess.getCartAdapterModel();
        cartAdapter = new CartListAdapter(getActivity() , cartAdapterModel);
        listViewCart = (ListView) view.findViewById(R.id.listViewCart);
        listViewCart.setAdapter(cartAdapter);
        dbAccess.close();

        cartItemId = cartAdapterModel.getId();

        final TextView tSum = view.findViewById(R.id.textViewSumCartFragment);
        if (cartAdapterModel != null){
            int sum = 0;
            Integer[] price = cartAdapterModel.getPrice();
            Integer[] amount = cartAdapterModel.getAmount();
            for(int i = 0; i < price.length; i++){
                sum += price[i] * amount[i];
            }
            tSum.setText(String.valueOf(sum));
        }

        listViewCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                getActivity().onBackPressed();
            }
        });

        ImageButton bBack = (ImageButton) view.findViewById(R.id.buttonBackCartFragment);
        ImageButton bSell = (ImageButton) view.findViewById(R.id.buttonSellCartFragment);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        bSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItemId.length != 0){
                    dbAccess.open();
                    dbAccess.sellItems(cartItemId);
                    dbAccess.close();
                    Toast.makeText(getActivity(),"Продано!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"Корзина пуста!", Toast.LENGTH_SHORT).show();
                }
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}
