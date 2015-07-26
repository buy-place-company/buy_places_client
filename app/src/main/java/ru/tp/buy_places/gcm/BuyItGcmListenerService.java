package ru.tp.buy_places.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.DealActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Deal;

/**
 * Created by Ivan on 21.07.2015.
 */
public class BuyItGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Data:" + data.toString());


        final String pushType = data.getString("push_type");
        JSONObject dealJsonObject = null;
        switch (pushType) {
            case "deal_new":
                try {
                    dealJsonObject = new JSONObject(data.getString("deal"));
                    Deal deal = Deal.fromJSONObject(dealJsonObject);
                    long rowId = deal.writeToDatabase(this);
                    deal.setRowId(rowId);
                    getContentResolver().notifyChange(BuyPlacesContract.Deals.CONTENT_URI, null);
                    sendNewDealNotification(deal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "deal_accept":
                try {
                    dealJsonObject = new JSONObject(data.getString("deal"));
                    Deal deal = Deal.fromJSONObject(dealJsonObject);
                    long rowId = deal.writeToDatabase(this);
                    deal.setRowId(rowId);
                    getContentResolver().notifyChange(BuyPlacesContract.Deals.CONTENT_URI, null);
                    sendDealAcceptedNotification(deal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "deal_reject":
                try {
                    dealJsonObject = new JSONObject(data.getString("deal"));
                    Deal deal = Deal.fromJSONObject(dealJsonObject);
                    long rowId = deal.writeToDatabase(this);
                    deal.setRowId(rowId);
                    getContentResolver().notifyChange(BuyPlacesContract.Deals.CONTENT_URI, null);
                    sendDealRejectedNotification(deal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "deal_revoke":
                try {
                    dealJsonObject = new JSONObject(data.getString("deal"));
                    Deal deal = Deal.fromJSONObject(dealJsonObject);
                    long rowId = deal.writeToDatabase(this);
                    deal.setRowId(rowId);
                    getContentResolver().notifyChange(BuyPlacesContract.Deals.CONTENT_URI, null);
                    sendDealRevokedNotification(deal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void sendDealRevokedNotification(Deal deal) {
        Intent intent = new Intent(this, DealActivity.class);
        intent.putExtra(DealActivity.EXTRA_DEAL_ROW_ID, deal.getRowId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String message = deal.getPlayerFrom().getUsername() + " больше не хочет выкупать " + deal.getVenue().getName();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Предложение сделки отозвали")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_cash_usd_white_18dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cash_multiple_white_48dp))
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendDealRejectedNotification(Deal deal) {
        Intent intent = new Intent(this, DealActivity.class);
        intent.putExtra(DealActivity.EXTRA_DEAL_ROW_ID, deal.getRowId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String message = deal.getPlayerTo().getUsername() + " отклонил Ваше предложение выкупить " + deal.getVenue().getName() + " за " + deal.getAmount();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("В сделке отказано")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_cash_usd_white_18dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cash_multiple_white_48dp))
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendDealAcceptedNotification(Deal deal) {
        Intent intent = new Intent(this, DealActivity.class);
        intent.putExtra(DealActivity.EXTRA_DEAL_ROW_ID, deal.getRowId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String message = deal.getPlayerFrom().getUsername() + " принял Ваше предложение выкупить " + deal.getVenue().getName() + " за " + deal.getAmount();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Сделка принята")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_cash_usd_white_18dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cash_multiple_white_48dp))
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNewDealNotification(Deal deal) {
        Intent intent = new Intent(this, DealActivity.class);
        intent.putExtra(DealActivity.EXTRA_DEAL_ROW_ID, deal.getRowId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String message = deal.getPlayerFrom().getUsername() + " хочет выкупить " + deal.getVenue().getName() + " за " + deal.getAmount();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Предложение сделки")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_cash_usd_white_18dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cash_multiple_white_48dp))
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
