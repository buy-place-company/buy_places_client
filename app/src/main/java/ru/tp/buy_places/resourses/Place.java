package ru.tp.buy_places.resourses;

import org.json.JSONObject;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Place implements Resource {
    private String mId;
    private long mCheckinsCount;
    private long mUsersCount;
    private long mTipCount;
    private String mName;
    private String mCategory;
    private String mType;
    private String mLevel;
    private long mOwner;
    private long mPrice;
    private float mLatitude;
    private float mLongitude;

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
