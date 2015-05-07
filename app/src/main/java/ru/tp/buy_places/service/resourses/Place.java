package ru.tp.buy_places.service.resourses;

import org.json.JSONObject;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Place implements Resource {
    private final String mId;
    private final long mCheckinsCount;
    private final long mUsersCount;
    private final long mTipCount;
    private final String mName;
    private final String mCategory;
    private final String mType;
    private final String mLevel;
    private final long mOwner;
    private final long mPrice;
    private final float mLatitude;
    private final float mLongitude;

    public Place(JSONObject placeData) {
        mId = placeData.optString("id");
        final JSONObject stats = placeData.optJSONObject("stats");
        mCheckinsCount = stats.optLong("checkinsCount");
        mUsersCount = stats.optLong("usersCount");
        mTipCount = stats.optLong("tipCount");
        mName = placeData.optString("name");
        mCategory = placeData.optString("category");
        mType = placeData.optString("type");
        mLevel = placeData.optString("level");
        mOwner = placeData.optLong("owner");
        mPrice = placeData.optLong("price");
        mLatitude = (float)placeData.optDouble("latitude");
        mLongitude = (float)placeData.optDouble("longitude");
    }

    public String getId() {
        return mId;
    }

    public long getCheckinsCount() {
        return mCheckinsCount;
    }

    public long getUsersCount() {
        return mUsersCount;
    }

    public long getTipCount() {
        return mTipCount;
    }

    public String getName() {
        return mName;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getType() {
        return mType;
    }

    public String getLevel() {
        return mLevel;
    }

    public long getOwner() {
        return mOwner;
    }

    public long getPrice() {
        return mPrice;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }
}
