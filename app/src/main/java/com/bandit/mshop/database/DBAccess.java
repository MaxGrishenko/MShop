package com.bandit.mshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bandit.mshop.adapters.CartAdapterModel;
import com.bandit.mshop.adapters.CategoryAdapterModel;
import com.bandit.mshop.adapters.ItemAdapterModel;

import java.util.ArrayList;
import java.util.List;

public class DBAccess {
    private DBHelper openHelper;
    private SQLiteDatabase db;
    private static DBAccess instance;
    Cursor c = null;

    // Таблички
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_ITEM = "item";
    // Общее колонки
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    // Колонки "item"
    public static final String COLUMN_CATEGORY_ID = "categoryId";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DISCOUNT = "discount";
    public static final String COLUMN_AMOUNT = "amount";


    // Для отображения категорий в ListView
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

        return new CategoryAdapterModel(id, name, image);
    }
    // Для отображение предметов в ListView по категориям
    public ItemAdapterModel getItemAdapterModel(int idCategory){
        List<Integer> idList = new ArrayList<Integer>();
        List<String> nameList = new ArrayList<String>();
        List<Integer> priceList = new ArrayList<>();
        List<byte[]> imageList = new ArrayList<>();

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
            } while (c.moveToNext());
        }

        int size = nameList.size();
        Integer[] id = idList.toArray(new Integer[size]);
        String[] name = nameList.toArray(new String[size]);
        Integer[] price = priceList.toArray(new Integer[size]);
        List<Bitmap> bitList = ByteToBitmap(imageList); //List<Byte[]> -> List<Bitmap> -> Bitmap[]
        Bitmap[] image = bitList.toArray(new Bitmap[size]);

        return new ItemAdapterModel(id, name, price, image);
    }
    // Конвертация List<byte[]> в List<Bitmap>
    private List<Bitmap> ByteToBitmap(List<byte[]> byteList){
        List<Bitmap> bitmapList = new ArrayList<>();
        for(byte[] byteArr: byteList){
            bitmapList.add(BitmapFactory.decodeByteArray(byteArr, 0,byteArr.length));
        }
        return bitmapList;
    }
    // Получение модели для отображения во фрагменте
    public ItemModel getItemInfo(int id){
        c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_ID + " like ?", new String[]{String.valueOf(id)}, null );
        c.moveToFirst();
        byte[] byteImage = c.getBlob(3);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        return new ItemModel(c.getInt(0),c.getString(2), bitmapImage, c.getString(4), c.getInt(5));
    }
    // Получение bitmap по id
    public Bitmap getItemImage(int id){
        c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_ID + " like ?", new String[]{String.valueOf(id)}, null );
        c.moveToFirst();
        byte[] byteImage = c.getBlob(3);
        return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
    }
    // Для отображение предметов в ListView в фрагменте корзина
    public CartAdapterModel getCartAdapterModel(){
        List<Integer> idList = new ArrayList<>();;
        List<String> nameList = new ArrayList<>();
        List<Integer> priceList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();

        c = db.rawQuery("select * from " + TABLE_ITEM + " where " + COLUMN_AMOUNT + " > 0", null );
        if (c.moveToFirst()){
            do{
                idList.add(c.getInt(0));
                nameList.add(c.getString(2));
                priceList.add(c.getInt(5));
                amountList.add(c.getInt(7));
            } while (c.moveToNext());
        }

        int size = idList.size();
        Integer[] id = idList.toArray(new Integer[size]);
        String[] name = nameList.toArray(new String[size]);
        Integer[] price = priceList.toArray(new Integer[size]);
        Integer[] amount = amountList.toArray(new Integer[size]);

        return new CartAdapterModel(id,name, price, amount);
    }
    // Изменение после добавления item в корзину
    public void updateItem(int id, int amount){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT, amount);
        db.update(TABLE_ITEM, cv, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
    }
    // Имитация продажи
    public void sellItems(Integer[] cartItemId) {
        for(int id : cartItemId){
            updateItem(id, 0);
        }
    }
    // Удаление item по id
    public void deleteItem(int id) {
        db.delete(TABLE_ITEM, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
    }


    // Работа с подключениями
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
