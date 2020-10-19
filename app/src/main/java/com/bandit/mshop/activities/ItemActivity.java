package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.bandit.mshop.R;
import com.bandit.mshop.database.DBAccess;

public class ItemActivity extends AppCompatActivity {
    DBAccess dbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }
}