package com.bandit.mshop.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Shop.db";
    private static final int SCHEMA = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

}
