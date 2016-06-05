package com.oxapps.tradenotifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BackgroundIntentService extends IntentService {
    private static final String LAST_REQUEST_KEY = "lastRequest";
    public static final String LAST_DELETE_KEY = "lastDelete";
    private static final String TIME_CREATED_KEY = "time_created";
    private static final String DESCRIPTION_KEY = "message";
    private static final String OFFER_STATE_KEY = "trade_offer_state";
    public static final String NOTIFICATION_CLICKED = "clicked";

    private String ORIGINAL_URL = "https://api.steampowered.com/IEconService/GetTradeOffers/v1/?key=APIKEYHERE&format=json&get_sent_offers=0&get_received_offers=1&get_descriptions=0&active_only=1&historical_only=0";
    OkHttpClient client = new OkHttpClient();



    public BackgroundIntentService() {
        super("BackgroundIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String apiKey = intent.getStringExtra(MainActivity.PREFS_KEY_API_KEY);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long lastDeleteTime = prefs.getLong(LAST_DELETE_KEY, 0L);

        String url = ORIGINAL_URL.replace("APIKEYHERE", apiKey);
        List<String> descriptions = new ArrayList<>();
        int newOfferCount = 0;

        try {
            String json = getTradeOffers(url);
            if(json.equals("")) {
                return;
            }
            JSONObject everything = new JSONObject(json);
            JSONArray tradeOffers = everything.getJSONObject("response").getJSONArray("trade_offers_received");
            for (int i = 0; i < tradeOffers.length(); i++) {
                JSONObject tradeOffer = tradeOffers.getJSONObject(i);
                long timeCreated = tradeOffer.getLong(TIME_CREATED_KEY);
                int state = tradeOffer.getInt(OFFER_STATE_KEY);
                if(timeCreated > lastDeleteTime) {
                    if(state != 2)  {
                        //state 2 is active, any other state is useless to us for initial purposes.
                        continue;
                    }
                    descriptions.add(tradeOffer.optString("message"));
                    newOfferCount++;
                } else {
                    //They are in chronological order so if the first was too early the rest will be too.
                    break;
                }
            }
        } catch(IOException | JSONException exception) {
            exception.printStackTrace();
        }

        if(newOfferCount > 0) {
            showNewTradeNotification(newOfferCount);
        }
    }


    private void showNewTradeNotification(int newOfferCount) {
        String contentText = "You have " + String.valueOf(newOfferCount) + " new trade offer";
        String append = (newOfferCount == 1) ? "" : "s";
        contentText = contentText.concat(append);

        Intent deleteIntent = new Intent(this, NotificationDeleteReceiver.class);
        PendingIntent pendingDeleteIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 5, deleteIntent, 0);

        Intent clickIntent = new Intent(this, NotificationDeleteReceiver.class);
        clickIntent.putExtra(NOTIFICATION_CLICKED, "clicked");
        PendingIntent pendingContentIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 6, clickIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notif_token)
                .setContentTitle("New Trade Offers")
                .setContentText(contentText)
                .setAutoCancel(true)
                .setVibrate(new long[]{300, 300, 300, 300, 300})
                .setDeleteIntent(pendingDeleteIntent)
                .setContentIntent(pendingContentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(10101, notificationBuilder.build());
    }


    private String getTradeOffers(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            return response.body().string();
        } else {
            if (response.code() == 403) {
                handleBadApiKeyError();
            }
            return "";
        }
    }

    private void handleBadApiKeyError() {
        showErrorNotification();
        //Cancel scheduled task so we don't get 100s of error notifications
        Intent backgroundIntent = new Intent(getApplicationContext(), BackgroundIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, backgroundIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void showErrorNotification() {
        Intent clickIntent = new Intent(this, MainActivity.class);
        clickIntent.putExtra(NOTIFICATION_CLICKED, "clicked");
        PendingIntent pendingContentIntent = PendingIntent.getActivity(this.getApplicationContext(), 6, clickIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notif_token)
                .setContentTitle("Error fetching trade offers")
                .setContentText("Steam API Key incorrect")
                .setAutoCancel(true)
                .setContentIntent(pendingContentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(403, notificationBuilder.build());
    }
}
