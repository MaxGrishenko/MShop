package com.bandit.mshop.others;

import android.content.SharedPreferences;

public class LateItem {
    public static void setLateItems(int idItem, SharedPreferences sPref){
        SharedPreferences.Editor editor = sPref.edit();
        if (sPref.contains("idItem4")){
            if (checkDuplicate(4, idItem, sPref)){
                editor.putInt("idItem4", sPref.getInt("idItem3", -1));
                editor.putInt("idItem3", sPref.getInt("idItem2", -1));
                editor.putInt("idItem2", sPref.getInt("idItem1", -1));
                editor.putInt("idItem1", idItem);
            }
        }
        else if (sPref.contains("idItem3")){
            if (checkDuplicate(3, idItem, sPref)){
                editor.putInt("idItem4", sPref.getInt("idItem3", -1));
                editor.putInt("idItem3", sPref.getInt("idItem2", -1));
                editor.putInt("idItem2", sPref.getInt("idItem1", -1));
                editor.putInt("idItem1", idItem);
            }
        }
        else if (sPref.contains("idItem2")){
            if (checkDuplicate(2, idItem, sPref)){
                editor.putInt("idItem3", sPref.getInt("idItem2", -1));
                editor.putInt("idItem2", sPref.getInt("idItem1", -1));
                editor.putInt("idItem1", idItem);
            }
        }
        else if (sPref.contains("idItem1")){
            if (checkDuplicate(1, idItem, sPref)){
                editor.putInt("idItem2", sPref.getInt("idItem1", -1));
                editor.putInt("idItem1", idItem);
            }
        }
        else{
            editor.putInt("idItem1", idItem);
        }
        editor.apply();
    }

    private static boolean checkDuplicate(int size, int idItem, SharedPreferences sPref){
        switch (size){
            case 1:
                if (idItem == sPref.getInt("idItem1", -1)){ return false; }
                break;
            case 2:
                if (idItem == sPref.getInt("idItem1", -1) || idItem == sPref.getInt("idItem2", -1)){ return false; }
                break;
            case 3:
                if (idItem == sPref.getInt("idItem1", -1) || idItem == sPref.getInt("idItem2", -1) || idItem == sPref.getInt("idItem3", -1)){ return false; }
                break;
            case 4:
                if (idItem == sPref.getInt("idItem1", -1) || idItem == sPref.getInt("idItem2", -1) || idItem == sPref.getInt("idItem3", -1) || idItem == sPref.getInt("idItem4", -1)){ return false; }
                break;
        }
        return true;
    }
}
