package ru.tp.buy_places.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.gcm.GCMRegistrationProcessorCreator;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 21.07.2015.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String GCM_REGISTRATION_COMPLETED = "ACTION_GCM_REGISTRATION_COMPLETED";
    public static final String GCM_REGISTRATION_RESULT = "GCM_REGISTRATION_RESULT";

    public RegistrationIntentService() {
        super(TAG);
    }



    public static void startGCMRegistrationService(Context context) {
        Intent intent = new Intent(context, RegistrationIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Processor processor = new GCMRegistrationProcessorCreator(this, new OnGCMProcessorResultListener(), token).createProcessor();
                processor.process();
                Log.i(TAG, "GCM Registration Token: " + token);
                subscribeTopics(token);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

    private class OnGCMProcessorResultListener implements Processor.OnProcessorResultListener {

        @Override
        public void send(Response response) {
            Intent intent = new Intent(GCM_REGISTRATION_COMPLETED);
            intent.putExtra(GCM_REGISTRATION_RESULT, response.getStatus());
            LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(intent);
        }
    }
}
