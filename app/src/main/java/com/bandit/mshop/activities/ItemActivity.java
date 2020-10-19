package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.bandit.mshop.R;
import com.bandit.mshop.adapters.ItemAdapterModel;
import com.bandit.mshop.adapters.ItemListAdapter;
import com.bandit.mshop.database.DBAccess;

public class ItemActivity extends AppCompatActivity {
    DBAccess dbAccess;
    ItemListAdapter itemAdapter;
    ListView listViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        dbAccess = DBAccess.getInstance(getApplicationContext());

        dbAccess.open();

        Bundle args = getIntent().getExtras();
        int idCategory = args.getInt("idCategory");

        ItemAdapterModel itemAdapterModel = dbAccess.getItemAdapterModel(idCategory);
        itemAdapter = new ItemListAdapter(this, itemAdapterModel);
        listViewItem = findViewById(R.id.listViewItem);
        listViewItem.setAdapter(itemAdapter);
        dbAccess.close();
    }
}