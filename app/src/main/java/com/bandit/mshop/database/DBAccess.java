package com.bandit.mshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bandit.mshop.adapters.CartAdapterModel;
import com.bandit.mshop.adapters.CategoryAdapterModel;
import com.bandit.mshop.adapters.ItemAdapterModel;
import com.bandit.mshop.adapters.PriceAdapterModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBAccess {
    private static final String TAG = "DBWork";
    private DBHelper openHelper;
    private SQLiteDatabase db;
    private static DBAccess instance;
    Cursor c = null;

    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_ITEM = "item";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_CATEGORY_ID = "categoryId";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DISCOUNT = "discount";
    public static final String COLUMN_AMOUNT = "amount";

    public CategoryAdapterModel getCategoryAdapterModel() {
        List<Integer> idList = new ArrayList<Integer>();
        List<String> nameList = new ArrayList<String>();
        List<byte[]> imageList = new ArrayList<byte[]>();
        c = db.rawQuery("select * from " + TABLE_CATEGORY, null );
        if (c.moveToFirst()){
            do{
                idList.add(c.getInt(0)) ;
                nameList.add(c.getString(1));
                imageList.add(c.getBlob(2));
            } while (c.moveToNext());
        }

        int size = nameList.size();
        Integer[] id = idList.toArray(new Integer[size]);
        String[] name = nameList.toArray(new String[size]);
        List<Bitmap> bitList = ByteToBitmap(imageList); //List<Byte[]> -> List<Bitmap> -> Bitmap[]
        Bitmap[] image = bitList.toArray(new Bitmap[size]);

        Log.d(TAG, "Get CategoryAdapterModel for ListView");
        return new CategoryAdapterModel(id, name, image);
    }
    public ItemAdapterModel getItemAdapterModel(int idCategory){
        List<Integer> idList = new ArrayList<Integer>();
        List<String> nameList = new ArrayList<String>();
        List<Integer> priceList = new ArrayList<>();
        List<byte[]> imageList = new ArrayList<>();
        List<Integer> discountList = new ArrayList<>();

        if (idCategory == 1){
            c = db.rawQuery("select * from " + TABLE_ITEM, null);
        }
        else c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_CATEGORY_ID + " like ?", new String[]{String.valueOf(idCategory)}, null );
        if (c.moveToFirst()){
            do{
                idList.add(c.getInt(0)) ;
                nameList.add(c.getString(2));
                priceList.add(c.getInt(5));
                imageList.add(c.getBlob(3));
                discountList.add(c.getInt(6));
            } while (c.moveToNext());
        }

        int size = nameList.size();
        Integer[] id = idList.toArray(new Integer[size]);
        String[] name = nameList.toArray(new String[size]);
        Integer[] price = priceList.toArray(new Integer[size]);
        List<Bitmap> bitList = ByteToBitmap(imageList); //List<Byte[]> -> List<Bitmap> -> Bitmap[]
        Bitmap[] image = bitList.toArray(new Bitmap[size]);
        Integer[] discount = discountList.toArray(new Integer[size]);

        Log.d(TAG, "Get ItemAdapterModel for ListView");
        return new ItemAdapterModel(id, name, price, image, discount);
    }
    private List<Bitmap> ByteToBitmap(List<byte[]> byteList){
        List<Bitmap> bitmapList = new ArrayList<>();
        for(byte[] byteArr: byteList){
            bitmapList.add(BitmapFactory.decodeByteArray(byteArr, 0,byteArr.length));
        }
        return bitmapList;
    }
    public ItemModel getItemInfo(int id){
        c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_ID + " like ?", new String[]{String.valueOf(id)}, null );
        c.moveToFirst();
        byte[] byteImage = c.getBlob(3);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);

        Log.d(TAG, "Get ItemModel by id-> " + String.valueOf(id));
        return new ItemModel(c.getInt(0),c.getString(2), bitmapImage, c.getString(4), c.getInt(5), c.getInt(6));
    }
    public Bitmap getItemImage(int id){
        c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_ID + " like ?", new String[]{String.valueOf(id)}, null );
        c.moveToFirst();
        byte[] byteImage = c.getBlob(3);
        return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
    }
    public CartAdapterModel getCartAdapterModel(){
        List<Integer> idList = new ArrayList<>();;
        List<String> nameList = new ArrayList<>();
        List<Integer> priceList = new ArrayList<>();
        List<Integer> discountList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();

        c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_AMOUNT + " > 0", null );
        if (c.moveToFirst()){
            do{
                idList.add(c.getInt(0));
                nameList.add(c.getString(2));
                priceList.add(c.getInt(5));
                discountList.add(c.getInt(6));
                amountList.add(c.getInt(7));
            } while (c.moveToNext());
        }

        int size = idList.size();
        Integer[] id = idList.toArray(new Integer[size]);
        String[] name = nameList.toArray(new String[size]);
        Integer[] price = priceList.toArray(new Integer[size]);
        Integer[] discount = discountList.toArray(new Integer[size]);
        Integer[] amount = amountList.toArray(new Integer[size]);


        Log.d(TAG, "Get CartAdapterModel for ListView");
        return new CartAdapterModel(id,name, price, discount, amount);
    }
    public void updateItem(int id, int amount){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT, amount);
        Log.d(TAG, "Update Item by id-> " + String.valueOf(id));
        db.update(TABLE_ITEM, cv, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void sellItems(Integer[] cartItemId) {
        for(int id : cartItemId){
            updateItem(id, 0);
        }
    }
    public void deleteItem(int id) {
        Log.d(TAG, "Delete Item by id-> " + String.valueOf(id));
        db.delete(TABLE_ITEM, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
    }
    public PriceAdapterModel getPriceListModel(){
        c = db.rawQuery("select * from item inner join category on item.categoryId = category.id" , null );
        List<Integer> idItemList = new ArrayList<>();
        List<String> categoryNameList = new ArrayList<>();
        List<String> itemNameList = new ArrayList<>();
        List<Integer> itemPriceList = new ArrayList<>();
        List<Integer> itemDiscountList = new ArrayList<>();

        if (c.moveToFirst()){
            do{
                idItemList.add(c.getInt(0));
                categoryNameList.add(c.getString(9));
                itemNameList.add(c.getString(2));
                itemPriceList.add(c.getInt(5));
                itemDiscountList.add(c.getInt(6));
            } while (c.moveToNext());
        }

        int size = idItemList.size();
        Integer[] idItem = idItemList.toArray(new Integer[size]);
        String[] categoryName = categoryNameList.toArray(new String[size]);
        String[] itemName = itemNameList.toArray(new String[size]);
        Integer[] itemPrice = itemPriceList.toArray(new Integer[size]);
        Integer[] itemDiscount = itemDiscountList.toArray(new Integer[size]);

        Log.d(TAG, "Get PriceAdapterModel for ListView");
        return new PriceAdapterModel(idItem, categoryName, itemName, itemPrice, itemDiscount);
    }
    public ItemModel getRandomDiscount(){
        List<Integer> idList = new ArrayList<>();
        c = db.rawQuery("select * from " + TABLE_ITEM, null );
        if (c.moveToFirst()){
            do{
                idList.add(c.getInt(0)) ;
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_DISCOUNT, 0);
                db.update(TABLE_ITEM, cv, COLUMN_ID + "= ?", new String[] {String.valueOf(c.getInt(0))});
            } while (c.moveToNext());
        }

        Random rand = new Random();
        int id = idList.get(rand.nextInt(idList.size()));
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DISCOUNT, 2);
        db.update(TABLE_ITEM, cv, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});

        return getItemInfo(id);
    }


    private DBAccess(Context context){
        this.openHelper=new DBHelper(context);
    }
    public static DBAccess getInstance(Context context){
        if (instance==null){
            instance = new DBAccess(context);
        }
        return instance;
    }
    public void open(){
        this.db = openHelper.getWritableDatabase();
    }
    public void close(){
        if (db != null){
            this.db.close();
        }
    }
}
