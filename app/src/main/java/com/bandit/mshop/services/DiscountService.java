package com.bandit.mshop.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.bandit.mshop.R;
import com.bandit.mshop.activities.CategoryActivity;
import com.bandit.mshop.database.DBAccess;
import com.bandit.mshop.database.ItemModel;
import com.bandit.mshop.others.NotificationEventReceiver;

public class DiscountService extends IntentService {
    private final int NOTIFICATION_ID = 100;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public DiscountService() {
        super(DiscountService.class.getSimpleName());
    }

    private void processStartNotification() {
        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.open();
        ItemModel item = dbAccess.getRandomDiscount();
        dbAccess.close();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder
                .setContentTitle(item.getName())
                .setContentText("Всего за: " + String.valueOf(item.getPrice() / item.getDiscount()) +"руб.")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.white))
                .setSmallIcon(R.drawable.ic_sell)
                .setLargeIcon(item.getImage());

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, CategoryActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
    private void processDeleteNotification(Intent intent) {
    }








    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, DiscountService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, DiscountService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

}