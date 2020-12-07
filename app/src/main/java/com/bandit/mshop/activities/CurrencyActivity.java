package com.bandit.mshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.bandit.mshop.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import static com.bandit.mshop.activities.CategoryActivity.APP_PREFERENCES;

public class CurrencyActivity extends AppCompatActivity {
    private static final String TAG = "CurrencyActivity";
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
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

    // CurrencyActivity lifecycle =================================================================
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://data.fixer.io/api/latest?access_key=f1ffbeb8a8ce9875745da8a249bd7fb8&symbols=RUB,BYN,USD&format=1")
                        .build();
                Response response = null;
                try{
                    response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    return jsonData;
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o){
                try {
                    JSONObject jsonResult = new JSONObject((String) o);

                    Map<String,Double> dict = new HashMap<String,Double>();
                    dict.put("RUB", Double.parseDouble(jsonResult.getJSONObject("rates").getString("RUB")));
                    dict.put("BYN", Double.parseDouble(jsonResult.getJSONObject("rates").getString("BYN")));
                    dict.put("USD", Double.parseDouble(jsonResult.getJSONObject("rates").getString("USD")));

                    TextView textViewRub = (TextView) findViewById(R.id.textRubCurrency);
                    TextView textViewByn = (TextView) findViewById(R.id.textBynCurrency);
                    TextView textViewUsd = (TextView) findViewById(R.id.textUsdCurrency);
                    textViewRub.setText("RUB : " + dict.get("RUB").toString());
                    textViewByn.setText("BYN : " + dict.get("BYN").toString());
                    textViewUsd.setText("USD : " + dict.get("USD").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

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