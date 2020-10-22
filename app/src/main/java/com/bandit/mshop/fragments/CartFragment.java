package com.bandit.mshop.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bandit.mshop.R;
import com.bandit.mshop.activities.CategoryActivity;
import com.bandit.mshop.adapters.CartAdapterModel;
import com.bandit.mshop.adapters.CartListAdapter;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.others.OnSwipeTouchListener;

import static com.bandit.mshop.activities.CategoryActivity.APP_PREFERENCES;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    CartListAdapter cartAdapter;
    ListView listViewCart;
    DBAccess dbAccess;
    Integer[] cartItemId;
    SharedPreferences sPref;

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
            Integer[] discount = cartAdapterModel.getDiscount();
            for(int i = 0; i < price.length; i++){
                if (discount[i] != 0){
                    sum += price[i] / discount[i] * amount[i];
                }
                else sum += price[i] * amount[i];
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
                    sPref = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    if(sPref.getBoolean("soundFlag", true)){
                        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.s_sell);
                        mp.start();
                    }

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
