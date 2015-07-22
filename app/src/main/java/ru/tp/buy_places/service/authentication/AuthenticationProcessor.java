package ru.tp.buy_places.service.authentication;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.service.network.Request;

/**
 * Created by Ivan on 16.07.2015.
 */
public class AuthenticationProcessor extends LoginProcessor {
    public static final String KET_AUTHENTICATION_RESULT = "KEY_AUTHENTICATION_RESULT";
    private final String mCode;
    protected AuthenticationProcessor(Context context, OnProcessorResultListener listener, String code) {
        super(context, listener);
        mCode = code;
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("code", mCode);
        return new Request(mContext, "/auth/vk", Request.RequestMethod.GET, params);
    }
}
