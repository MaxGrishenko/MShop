package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bandit.mshop.R;
import com.bandit.mshop.adapters.ItemAdapterModel;
import com.bandit.mshop.adapters.ItemListAdapter;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.fragments.CartFragment;
import com.bandit.mshop.fragments.HelpFragment;
import com.bandit.mshop.fragments.ItemFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    private static final String TAG = "ItemActivity";
    DBAccess dbAccess;
    ItemListAdapter itemAdapter;
    ListView listViewItem;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    boolean isFragmentActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Log.d(TAG,"onCreate");

        dbAccess = DBAccess.getInstance(getApplicationContext());

        Bundle args = getIntent().getExtras();
        int idCategory = args.getInt("idCategory");

        dbAccess.open();
        ItemAdapterModel itemAdapterModel = dbAccess.getItemAdapterModel(idCategory);
        itemAdapter = new ItemListAdapter(this, itemAdapterModel);
        listViewItem = findViewById(R.id.listViewItem);
        listViewItem.setAdapter(itemAdapter);
        dbAccess.close();

        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Закидываем во ItemFragment список id товаров
                Integer idItemCurrent = (Integer) itemAdapter.getItem(i);
                ArrayList<Integer> idItemList = new ArrayList<>();

                for (int q = 0; q < itemAdapter.getCount(); q++){
                    idItemList.add((Integer) itemAdapter.getItem(q));
                }

                Bundle bundle = new Bundle();
                bundle.putInt("indexItem", i);
                bundle.putIntegerArrayList("idItemList", idItemList);

                fragmentManager=getSupportFragmentManager();
                changeFragment("item", R.id.containerItem, bundle);
            }
        });

        FloatingActionButton fab = findViewById(R.id.buttonCartItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment("cart", R.id.containerItem, null);
            }
        });
    }

    // Fragment work ===============================================================================
    private void changeFragment(String fragmentType, int fragmentContainer, Bundle data){
        Fragment fragment;
        fragmentManager=getSupportFragmentManager();
        if (isFragmentActive){
            if (isFragmentActive){
                fragment = fragmentManager.findFragmentById(fragmentContainer);
                if (fragment instanceof HelpFragment && fragmentType == "cart"){
                    removeFragment(fragmentContainer);
                }
                else if (fragment instanceof CartFragment && fragmentType == "help"){
                    removeFragment(fragmentContainer);
                }
                else if (fragment instanceof ItemFragment && fragmentType != "item"){
                    removeFragment(fragmentContainer);
                }
                else fragmentType = "null";
            }
        }
        switch (fragmentType){
            case "item":
                fragment = new ItemFragment();
                fragment.setArguments(data);
                break;
            case "help":
                fragment = new HelpFragment();
                break;
            case "cart":
                fragment = new CartFragment();
                break;
            default: return;
        }

        isFragmentActive = true;
        listViewItem.setEnabled(false);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void removeFragment(int fragmentContainer){
        isFragmentActive = false;
        listViewItem.setEnabled(true);
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(fragmentContainer);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (!isFragmentActive){
            super.onBackPressed();
        }
        else{
            removeFragment(R.id.containerItem);
        }
    }
    // ToolBar menu ================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_help:
                changeFragment("help", R.id.containerItem, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Отслеживание жизненного цикла CategoryActivity ==============================================
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