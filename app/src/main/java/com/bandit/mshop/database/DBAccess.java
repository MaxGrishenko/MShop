package com.bandit.mshop.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bandit.mshop.adapters.CategoryAdapterModel;

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
    // Конвертация List<byte[]> в List<Bitmap>
    private List<Bitmap> ByteToBitmap(List<byte[]> byteList){
        List<Bitmap> bitmapList = new ArrayList<>();
        for(byte[] byteArr: byteList){
            bitmapList.add(BitmapFactory.decodeByteArray(byteArr, 0,byteArr.length));
        }
        return bitmapList;
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
