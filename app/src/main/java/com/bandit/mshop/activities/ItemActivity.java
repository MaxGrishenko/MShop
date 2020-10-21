package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bandit.mshop.R;
import com.bandit.mshop.adapters.ItemAdapterModel;
import com.bandit.mshop.adapters.ItemListAdapter;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.fragments.CartFragment;
import com.bandit.mshop.fragments.HelpFragment;
import com.bandit.mshop.fragments.ImageFragment;
import com.bandit.mshop.fragments.ItemFragment;
import com.bandit.mshop.others.LateItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.bandit.mshop.activities.CategoryActivity.APP_PREFERENCES;

public class ItemActivity extends AppCompatActivity {
    private static final String TAG = "ItemActivity";
    DBAccess dbAccess;
    ItemListAdapter itemAdapter;
    ListView listViewItem;
    SharedPreferences sPref;
    int idCategory;

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
        idCategory = args.getInt("idCategory");

        dbAccess.open();
        ItemAdapterModel itemAdapterModel = dbAccess.getItemAdapterModel(idCategory);
        itemAdapter = new ItemListAdapter(this, itemAdapterModel);
        listViewItem = findViewById(R.id.listViewItem);
        listViewItem.setAdapter(itemAdapter);
        dbAccess.close();

        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Create list of item id
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
                fragmentManager=getSupportFragmentManager();
                changeFragment("cart", R.id.containerItem, null);
            }
        });
    }

    // Button clicks for CartFragment ==============================================================
    public void clickChangeAmountCart(View view){
        RelativeLayout parentRow = (RelativeLayout) view.getParent();
        TextView textViewAmount = (TextView) parentRow.findViewById(R.id.textViewAmountCart);
        TextView textViewPrice = (TextView) parentRow.findViewById(R.id.textViewPriceCart);
        TextView textViewTotal = (TextView) parentRow.findViewById(R.id.textViewTotalCart);
        TextView textViewSum = (TextView) findViewById(R.id.textViewSumCartFragment);

        Integer amount = Integer.parseInt((String)textViewAmount.getText());
        Integer sum = Integer.parseInt((String) textViewSum.getText());
        Integer price = Integer.parseInt((String)textViewPrice.getText());

        switch (view.getId()){
            case R.id.buttonAddCart:
                if (amount != 9){
                    amount++;
                    textViewSum.setText(String.valueOf(sum + price));
                }
                else return;
                break;
            case R.id.buttonSubCart:
                if (amount != 1){
                    amount--;
                    textViewSum.setText(String.valueOf(sum - price));
                }
                else return;
                break;
        }
        dbAccess.open();
        dbAccess.updateItem((int) view.getTag(), amount);
        dbAccess.close();
        textViewAmount.setText(String.valueOf(amount));
        textViewTotal.setText(String.valueOf(amount * price));
    }
    public void clickDeleteCart(View view){
        int id = (int) view.getTag();
        onBackPressed();
        dbAccess.open();
        dbAccess.updateItem(id, 0);
        dbAccess.close();
        fragmentManager=getSupportFragmentManager();
        Toast.makeText(this,"Товар убран из корзины!", Toast.LENGTH_SHORT).show();
        changeFragment("cart", R.id.containerItem, null);
    }
    //==============================================================================================

    // Button clicks for CartFragment ==============================================================
    public void buttonDelete(View view){
        onBackPressed();
        dbAccess.open();
        dbAccess.deleteItem((int) view.getTag());
        Toast.makeText(this,"Товар удалён из БД!", Toast.LENGTH_SHORT).show();
        ItemAdapterModel itemAdapterModel = dbAccess.getItemAdapterModel(idCategory);
        itemAdapter = new ItemListAdapter(this, itemAdapterModel);
        listViewItem = findViewById(R.id.listViewItem);
        listViewItem.setAdapter(itemAdapter);
        dbAccess.close();
        deleteLateItems((int) view.getTag());
    }
    private void deleteLateItems(int id){
        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        List<Integer> idList = new ArrayList<>();
        for (int i = 4; i > 0; i--){
            String item = "idItem" + String.valueOf(i);
            if (sPref.contains(item)){
                if (sPref.getInt(item, -1) != id){
                    idList.add(sPref.getInt(item,-1));
                }
            }
        }
        sPref.edit().clear().commit();
        for (int idItem: idList) {
            LateItem.setLateItems(idItem, sPref);
        }
    }
    //==============================================================================================

    public void clickShowImage(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("itemId", (int) view.getTag());
        int i = (int) view.getTag();
        changeFragment("image", R.id.containerItem, bundle);
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
                else if (fragment instanceof ImageFragment && fragmentType != "item"){
                    removeFragment(fragmentContainer);
                }
                else fragmentType = "null";
            }
        }
        switch (fragmentType){
            case "image":
                fragment = new ImageFragment();
                fragment.setArguments(data);
                break;
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
    //==============================================================================================

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
    //==============================================================================================

    // ItemActivity lifecycle ==================================================================
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