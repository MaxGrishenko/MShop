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

public class CategoryListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] name;
    private final Bitmap[] image;

    public CategoryListAdapter(Activity context, CategoryAdapterModel category){
        super(context, R.layout.listview_category_row , category.getId());
        this.context = context;
        this.name = category.getName();
        this.image = category.getImage();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_category_row, null,true);

        TextView textViewName = rowView.findViewById(R.id.textViewName);
        ImageView imageView = rowView.findViewById(R.id.imageView);

        textViewName.setText(this.name[position]);
        imageView.setImageBitmap(image[position]);

        return rowView;
    };
}
