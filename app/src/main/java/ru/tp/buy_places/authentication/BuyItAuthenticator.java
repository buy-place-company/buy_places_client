package ru.tp.buy_places.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 22.05.2015.
 */
public class BuyItAuthenticator extends AbstractAccountAuthenticator {
    private final Context mContext;

    public BuyItAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent( mContext, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_TOKEN_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        if (options != null) {
            bundle.putAll(options);
        }
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(LoginActivity.EXTRA_TOKEN_TYPE, authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    @NonNull
    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        Bundle result = super.getAccountRemovalAllowed(response, account);
        InstanceID instanceID = InstanceID.getInstance(mContext);
        String token = null;
        try {
            token = instanceID.getToken(mContext.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Map<String, String> params = new HashMap<>();
            params.put("reg_id", token);
            Request unregTokenRequest = new Request(mContext, "/push/unreg", Request.RequestMethod.POST, params);
            unregTokenRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Request request = new Request(mContext, "/auth/logout", Request.RequestMethod.POST, new HashMap<String, String>());
        JSONObject responseJSONObject = request.execute();
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        Response logoutResonse = new Response(status, message, null);
        if (logoutResonse.getStatus() == 200) {
            ((CookieManager) CookieManager.getDefault()).getCookieStore().removeAll();
            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
            if (cookieManager.hasCookies())
                cookieManager.removeAllCookie();
            mContext.getContentResolver().delete(BuyPlacesContract.Deals.CONTENT_URI, null, null);
            mContext.getContentResolver().delete(BuyPlacesContract.Places.CONTENT_URI, null, null);
            mContext.getContentResolver().delete(BuyPlacesContract.Players.CONTENT_URI, null, null);
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        }
        else {
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        }
        return result;
    }
}
