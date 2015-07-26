package ru.tp.buy_places.service.network;

import java.util.Map;

import ru.tp.buy_places.service.resourses.Resource;

/**
 * Created by Ivan on 22.04.2015.
 */
public class Response {
    public static final int RESULT_OK = 200;
    public static final int RESULT_LOGIN_FAILED = 602;
    public static final int RESULT_UNAVAILABLE = 0;
    private final int mStatus;
    private final String mMessage;
    private final Map<String, Resource> mData;

    public Response(int status, String message, Map<String, Resource> data) {
        mStatus = status;
        mMessage = message;
        mData = data;
    }

    public Map<String, Resource> getData() {
        return mData;
    }

    public int getStatus() {
        return mStatus;
    }
}