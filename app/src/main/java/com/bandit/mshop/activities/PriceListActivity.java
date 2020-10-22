package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.bandit.mshop.R;
import com.bandit.mshop.adapters.CategoryAdapterModel;
import com.bandit.mshop.adapters.CategoryListAdapter;
import com.bandit.mshop.adapters.ItemListAdapter;
import com.bandit.mshop.adapters.PriceAdapterModel;
import com.bandit.mshop.adapters.PriceListAdapter;
import com.bandit.mshop.database.DBAccess;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import static com.bandit.mshop.activities.CategoryActivity.APP_PREFERENCES;

public class PriceListActivity extends AppCompatActivity {
    private static final String TAG = "MPriceListActivity";
    DBAccess dbAccess;
    PriceListAdapter priceAdapter;
    ListView listViewPrice;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        dbAccess = DBAccess.getInstance(getApplicationContext());

        dbAccess.open();
        PriceAdapterModel priceAdapterModel = dbAccess.getPriceListModel();
        priceAdapter = new PriceListAdapter(this, priceAdapterModel);
        listViewPrice = findViewById(R.id.listViewPrice);
        listViewPrice.setAdapter(priceAdapter);
        dbAccess.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sPref.edit();
                editor1.putBoolean("soundFlag", true);
                editor1.apply();
                Toast.makeText(this,"Звуковые эффекты включены", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sPref.edit();
                editor2.putBoolean("soundFlag", false);
                editor2.apply();
                Toast.makeText(this,"Звуковые эффекты выключены", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // PriceListActivity lifecycle =================================================================
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    };
    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
    };
    @Override
    public void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart");
    };
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    };
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    };
    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Log.d(TAG, "onSaveInstanceState");
    };
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    };
    //==============================================================================================
}