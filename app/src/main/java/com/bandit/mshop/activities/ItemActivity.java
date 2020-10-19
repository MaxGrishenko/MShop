package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
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

        dbAccess = DBAccess.getInstance(getApplicationContext());

        dbAccess.open();

        Bundle args = getIntent().getExtras();
        int idCategory = args.getInt("idCategory");

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
    // =============================================================================================
}