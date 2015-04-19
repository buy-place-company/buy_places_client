package ru.tp.buy_places;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.List;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;

/**
 * Created by Ivan on 20.04.2015.
 */
public class RestMethodFactory {


    private static RestMethodFactory instance;
    private static final UriMatcher URI_MATCHER;
    private static final int PLACES = 0;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(BuyPlacesContract.AUTHORITY, BuyPlacesContract.Places.TABLE_NAME, PLACES);
    }

    private final Context mContext;

    private RestMethodFactory (Context context) {
        mContext = context;
    }

    public static RestMethodFactory get(Context context) {
        if (instance == null) {
            instance = new RestMethodFactory(context);
        }
        return instance;
    }


    public RestMethod getRestMethod(Uri resourceUri, Method method,
                                    Map<String, List<String>> headers, byte[] body) {


        switch (URI_MATCHER.match(resourceUri)) {
            case PLACES:
                if (method == Method.GET) {
                    return new GetNearestPlacesRestMethod(mContext);
                }
                break;
        }

        return null;
    }


    public enum Method {
        GET, POST, PUT, DELETE
    }
}
