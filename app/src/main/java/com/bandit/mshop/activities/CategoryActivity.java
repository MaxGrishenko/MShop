package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bandit.mshop.R;
import com.bandit.mshop.adapters.CategoryAdapterModel;
import com.bandit.mshop.adapters.CategoryListAdapter;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.fragments.CartFragment;
import com.bandit.mshop.fragments.HelpFragment;
import com.bandit.mshop.fragments.ImageFragment;
import com.bandit.mshop.fragments.ItemFragment;
import com.bandit.mshop.others.LateItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "CategoryActivity";
    public boolean soundFlag = true;

    DBAccess dbAccess;
    CategoryListAdapter categoryAdapter;
    ListView listViewCategory;

    public static final String APP_PREFERENCES = "myLastItems";
    SharedPreferences sPref;
    LinearLayout l1;
    CircularImageView ci1;
    CircularImageView ci2;
    CircularImageView ci3;
    CircularImageView ci4;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    boolean isFragmentActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Log.d(TAG,"onCreate");

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

        FloatingActionButton fab = findViewById(R.id.buttonCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager=getSupportFragmentManager();
                changeFragment("cart", R.id.containerCategory, null);
            }
        });
    }
    // Start new activity with right category
    private void showCategoryItems(int idCategory){
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("idCategory", idCategory);
        startActivity(intent);
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
        changeFragment("cart", R.id.containerCategory, null);
    }
    //==============================================================================================

    // Remove from DB and update LateItems =========================================================
    public void buttonDelete(View view){
        onBackPressed();
        dbAccess.open();
        dbAccess.deleteItem((int) view.getTag());
        Toast.makeText(this,"Товар удалён из БД!", Toast.LENGTH_SHORT).show();
        CategoryAdapterModel categoryAdapterModel = dbAccess.getCategoryAdapterModel();
        categoryAdapter = new CategoryListAdapter(this, categoryAdapterModel);
        listViewCategory = findViewById(R.id.listViewCategory);
        listViewCategory.setAdapter(categoryAdapter);
        dbAccess.close();
        deleteLateItems((int) view.getTag());
        setLateItems();
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

    // LateItems work ==============================================================================
    private void setLateItems(){
        l1 = findViewById(R.id.lineaLayoutLate);
        ci1 = findViewById(R.id.ciLate1);
        ci2 = findViewById(R.id.ciLate2);
        ci3 = findViewById(R.id.ciLate3);
        ci4 = findViewById(R.id.ciLate4);
        setImageLate();
    }
    private void setImageLate(){
        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        lateClearAnimation();
        lateSetAllVisibility(View.VISIBLE);
        lateClearAnimation();

        dbAccess.open();
        if (sPref.contains("idItem4")){
            ci1.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem1", -1)));
            ci2.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem2", -1)));
            ci3.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem3", -1)));
            ci4.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem4", -1)));
        }
        else if (sPref.contains("idItem3")){
            ci1.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem1", -1)));
            ci2.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem2", -1)));
            ci3.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem3", -1)));
            ci4.setVisibility(View.GONE);
        }
        else if (sPref.contains("idItem2")) {
            ci1.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem1", -1)));
            ci2.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem2", -1)));
            ci3.setVisibility(View.GONE);
            ci4.setVisibility(View.GONE);
        }
        else if (sPref.contains("idItem1")){
            ci1.setImageBitmap(dbAccess.getItemImage(sPref.getInt("idItem1", -1)));
            ci2.setVisibility(View.GONE);
            ci3.setVisibility(View.GONE);
            ci4.setVisibility(View.GONE);
        }
        else{
            lateSetAllVisibility(View.GONE);
        }
        dbAccess.close();
    }
    private void lateClearAnimation(){
        l1.clearAnimation();
        ci1.clearAnimation();
        ci2.clearAnimation();
        ci3.clearAnimation();
        ci4.clearAnimation();
    }
    private void lateSetAllVisibility(int visibility){
        l1.setVisibility(visibility);
        ci1.setVisibility(visibility);
        ci2.setVisibility(visibility);
        ci3.setVisibility(visibility);
        ci4.setVisibility(visibility);
    }
    public void buttonLate(View view){
        int indexItem = 0;
        int size = 0;
        switch (view.getId()){
            case R.id.ciLate1:
                indexItem = 0;
                break;
            case R.id.ciLate2:
                indexItem = 1;
                break;
            case R.id.ciLate3:
                indexItem = 2;
                break;
            case R.id.ciLate4:
                indexItem = 3;
                break;
        }
        if (sPref.contains("idItem4")){ size = 4;}
        else if (sPref.contains("idItem3")){ size = 3;}
        else if (sPref.contains("idItem2")){ size = 2;}
        else if (sPref.contains("idItem1")){ size = 1;}

        ArrayList<Integer> idItemList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            String idItem = "idItem";
            idItemList.add(sPref.getInt(idItem+String.valueOf(i+1),-1));
        }

        Bundle bundle = new Bundle();
        bundle.putInt("indexItem", indexItem);
        bundle.putIntegerArrayList("idItemList", idItemList);

        fragmentManager=getSupportFragmentManager();
        changeFragment("item", R.id.containerCategory, bundle);

    }
    //==============================================================================================

    // Show ImageFragment ==========================================================================
    public void clickShowImage(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("itemId", (int) view.getTag());
        int i = (int) view.getTag();
        changeFragment("image", R.id.containerCategory, bundle);
    }
    //==============================================================================================

    // Fragment work ===============================================================================
    private void changeFragment(String fragmentType, int fragmentContainer, Bundle data){
        Fragment fragment;
        fragmentManager=getSupportFragmentManager();
        if (fragmentType == "close"){
            if (isFragmentActive){
                isFragmentActive = false;
                removeFragment(fragmentContainer);
            }
            return;
        }

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

        switch (fragmentType) {
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
            default:
                return;
        }

        isFragmentActive = true;
        listViewCategory.setEnabled(false);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
    private void removeFragment(int fragmentContainer){
        isFragmentActive = false;
        listViewCategory.setEnabled(true);
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
            removeFragment(R.id.containerCategory);
            setLateItems();
        }
    }
    //==============================================================================================

    // ToolBar menu ================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_help:
                fragmentManager=getSupportFragmentManager();
                changeFragment("help", R.id.containerCategory, null);
                return true;
            case R.id.item_price_list:
                changeFragment("close", R.id.containerCategory, null);
                Intent intent = new Intent(this, PriceListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //==============================================================================================

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

    // CategoryActivity lifecycle ==================================================================
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
        setLateItems();
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