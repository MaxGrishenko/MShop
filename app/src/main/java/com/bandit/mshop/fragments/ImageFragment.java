package com.bandit.mshop.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bandit.mshop.R;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.others.OnScaleTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {
    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        int itemId = getArguments().getInt("itemId");

        DBAccess dbAccess = DBAccess.getInstance(getActivity().getApplicationContext());
        dbAccess.open();
        Bitmap image = dbAccess.getItemImage(itemId);
        dbAccess.close();

        final ImageView imageView = view.findViewById(R.id.imageViewImage);
        imageView.setOnTouchListener(new OnScaleTouchListener(getContext()));
        acceptImage(imageView, image);

        FloatingActionButton fab = view.findViewById(R.id.buttonCloseImage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void acceptImage (ImageView iv, Bitmap bm){
        iv.setImageBitmap(bm);
    }
}