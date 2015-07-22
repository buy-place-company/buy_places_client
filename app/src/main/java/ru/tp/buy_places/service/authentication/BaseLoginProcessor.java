package ru.tp.buy_places.service.authentication;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.network.Request;

/**
 * Created by Ivan on 22.07.2015.
 */
public class BaseLoginProcessor extends LoginProcessor {
    private final String mUsername;
    private final String mPassword;

    protected BaseLoginProcessor(Context context, OnProcessorResultListener listener, String username, String password) {
        super(context, listener);
        mUsername = username;
        mPassword = password;
    }

    @Override
    protected Request prepareRequest() {
        String path = "";
        Map<String, String> params = new HashMap<>();
        params.put("", mUsername);
        params.put("", mPassword);
        return new Request(mContext, path, Request.RequestMethod.POST, params);
    }
}
