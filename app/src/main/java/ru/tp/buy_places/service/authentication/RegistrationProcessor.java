package ru.tp.buy_places.service.authentication;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.network.Request;

/**
 * Created by Ivan on 22.07.2015.
 */
public class RegistrationProcessor extends LoginProcessor {
    private final String mUsername;
    private final String mEmail;
    private final String mPassword;

    protected RegistrationProcessor(Context context, OnProcessorResultListener listener, String email, String username, String password) {
        super(context, listener);
        mUsername = username;
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Request prepareRequest() {
        String path = "/auth/signup";
        Map<String,String> params = new HashMap<>();
        params.put("email", mEmail);
        params.put("password", mPassword);
        params.put("name", mUsername);
        return new Request(mContext, path, Request.RequestMethod.POST, params);
    }
}
