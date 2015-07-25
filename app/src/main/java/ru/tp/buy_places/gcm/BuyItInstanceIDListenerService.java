package ru.tp.buy_places.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Ivan on 21.07.2015.
 */
public class BuyItInstanceIDListenerService extends InstanceIDListenerService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        RegistrationIntentService.startGCMRegistrationService(this);
    }
    // [END refresh_token]
}
