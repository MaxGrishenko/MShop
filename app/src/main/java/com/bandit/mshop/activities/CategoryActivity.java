package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bandit.mshop.R;
import com.bandit.mshop.adapters.CategoryAdapterModel;
import com.bandit.mshop.adapters.CategoryListAdapter;
import com.bandit.mshop.database.DBAccess;

public class CategoryActivity extends AppCompatActivity {
    DBAccess dbAccess;
    CategoryListAdapter categoryAdapter;
    ListView listViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        dbAccess = DBAccess.getInstance(getApplicationContext());

        dbAccess.open();
        CategoryAdapterModel categoryAdapterModel = dbAccess.getCategoryAdapterModel();
        categoryAdapter = new CategoryListAdapter(this, categoryAdapterModel);
        listViewCategory = findViewById(R.id.listViewCategory);
        listViewCategory.setAdapter(categoryAdapter);
        dbAccess.close();

        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer idCategory = (Integer) categoryAdapter.getItem(i);
                showCategoryItems(idCategory);
            }
        });
    }
    private void showCategoryItems(int idCategory){
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("idCategory", idCategory);
        startActivity(intent);
    }
}