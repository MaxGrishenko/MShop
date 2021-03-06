package com.bandit.mshop.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bandit.mshop.R;
import com.bandit.mshop.activities.CategoryActivity;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.database.ItemModel;
import com.bandit.mshop.others.LateItem;
import com.bandit.mshop.others.OnScaleTouchListener;
import com.bandit.mshop.others.OnSwipeTouchListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bandit.mshop.activities.CategoryActivity.APP_PREFERENCES;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {
    private static final String TAG = "Gest";
    DBAccess dbAccess;
    int price;
    int discount;
    int indexItem;
    ArrayList<Integer> idItemList;
    SharedPreferences sPref;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        sPref = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        dbAccess = DBAccess.getInstance(getActivity().getApplicationContext());

        indexItem = getArguments().getInt("indexItem");
        idItemList = getArguments().getIntegerArrayList("idItemList");

        final CircularImageView ciPhoto = view.findViewById(R.id.circleImageViewPhotoFragment);
        final TextView tName = view.findViewById(R.id.textViewNameFragment);
        final TextView tPrice = view.findViewById(R.id.textViewPriceFragment);
        final TextView tDescription = view.findViewById(R.id.textViewDescriptionFragment);
        final TextView tAmount = view.findViewById(R.id.textViewAmountFragment);
        final TextView tTotalPrice = view.findViewById(R.id.textViewTotalPriceFragment);
        final ImageButton bDelete = view.findViewById(R.id.buttonDeleteFragment);
        setItemInfo(view, ciPhoto, tName, tPrice, tDescription, tAmount, tTotalPrice, bDelete);

        ImageButton bBack = view.findViewById(R.id.buttonBackFragment);
        ImageButton bToCart = view.findViewById(R.id.buttonCartFragment);
        ImageButton bAdd = view.findViewById(R.id.buttonAddAmountFragment);
        ImageButton bSub = view.findViewById(R.id.buttonSubAmountFragment);
        ImageButton bLeft = view.findViewById(R.id.buttonLeftFragment);
        ImageButton bRight = view.findViewById(R.id.buttonRightFragment);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        bToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAccess.open();
                dbAccess.updateItem(idItemList.get(indexItem), Integer.parseInt((String) tAmount.getText()));
                dbAccess.close();
                Toast.makeText(getActivity(),"Товар добавлен в корзину!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt((String) tAmount.getText());
                if (amount != 9) {
                    amount += 1;
                    tAmount.setText(String.valueOf(amount));
                    tTotalPrice.setText(String.valueOf(price / discount * amount));
                }
            }
        });
        bSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt((String) tAmount.getText());
                if (amount != 1) {
                    amount -= 1;
                    tAmount.setText(String.valueOf(amount));
                    tTotalPrice.setText(String.valueOf(price / discount * amount));
                }
            }
        });
        bLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = idItemList.size() - 1;
                if (indexItem == 0) {
                    indexItem = size;
                } else indexItem -= 1;
                setItemInfo(view, ciPhoto, tName, tPrice, tDescription, tAmount, tTotalPrice, bDelete);
            }
        });
        bRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexItem == idItemList.size() - 1) {
                    indexItem = 0;
                } else indexItem += 1;
                setItemInfo(view, ciPhoto, tName, tPrice, tDescription, tAmount, tTotalPrice, bDelete);
            }
        });
        final RelativeLayout rlGest = view.findViewById(R.id.rlGest);
        rlGest.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                bBack.performClick();
            }
            public void onSwipeRight() {
                bLeft.performClick();
            }
            public void onSwipeLeft() {
                bRight.performClick();
            }
            public void onSwipeBottom() {
                bToCart.performClick();
            }

        });

        return view;
    }

    // From id get item
    private void setItemInfo(View view, CircularImageView ciPhoto, TextView tName,
                             TextView tPrice, TextView tDescription, TextView tAmount, TextView tTotalPrice, ImageButton bDelete) {
        dbAccess.open();
        ItemModel item = dbAccess.getItemInfo(idItemList.get(indexItem));
        bDelete.setTag(item.getId());
        dbAccess.close();
        price = item.getPrice();

        ciPhoto.setTag((int) item.getId());
        ciPhoto.setImageBitmap(item.getImage());
        tName.setText(item.getName());

        if (item.getDiscount() != 0){
            discount = item.getDiscount();

        }
        else{
            discount = 1;
        }

        tPrice.setText(String.valueOf(price / discount));
        tTotalPrice.setText(String.valueOf(price / discount));
        tAmount.setText(String.valueOf(1));
        tDescription.setText(item.getDescription());
        int idItem = idItemList.get(indexItem);
        LateItem.setLateItems(idItem, sPref);
    }
}