package ru.tp.buy_places.service.resourses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONObject;

import ru.tp.buy_places.content_provider.BuyPlacesContract;

/**
 * Created by Ivan on 19.04.2015.
 */
public class Place implements Resource {
    private long mRowId;
    private final String mId;
    private final long mCheckinsCount;
    private final long mUsersCount;
    private final long mTipCount;
    private final String mName;
    private final String mCategory;
    private final String mType;
    private final int mLevel;
    private final long mOwner;
    private final long mPrice;
    private final double mLatitude;
    private final double mLongitude;

    private boolean mIsAroundThePoint;
    private boolean mIsAroundThePlayer;
    private boolean mIsInOwnership;
    private boolean mIsVisitedInThePast;

    private boolean mIsAroundThePointIsSet = false;
    private boolean mIsAroundThePlayerIsSet = false;
    private boolean mIsInOwnershipIsSet = false;
    private boolean mIsVisitedInThePastIsSet = false;

    public static Place fromJSONObject(JSONObject placeData) {
        String id = placeData.optString("id");
        final JSONObject stats = placeData.optJSONObject("stats");
        long checkinsCount = stats.optLong("checkinsCount");
        long usersCount = stats.optLong("usersCount");
        long tipCount = stats.optLong("tipCount");
        String name = placeData.optString("name");
        String category = placeData.optString("category");
        String type = placeData.optString("type");
        int level = placeData.optInt("level");
        long owner = placeData.optLong("owner");
        long price = placeData.optLong("price");
        double latitude = placeData.optDouble("latitude");
        double longitude = placeData.optDouble("longitude");
        return new Place(id, checkinsCount, usersCount, tipCount, name, category, type, level, owner, price, latitude, longitude);
    }

    public static Place fromCursor(Cursor row) {
        long rowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Places._ID));
        String id = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ID));
        long checkinsCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT));
        long usersCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_USERS_COUNT));
        long tipCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_TIP_COUNT));
        String name = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_NAME));
        String category = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_CATEGORY));
        String type = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_TYPE));
        int level = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_LEVEL));
        long owner = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_OWNER));
        long price = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_PRICE));
        double latitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_LATITUDE));
        double longitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_LONGITUDE));
        final boolean isAroundThePoint = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT)) != 0;
        final boolean isAroundThePlayer = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER)) != 0;
        final boolean isVisitedInThePast = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_IS_VISITED_IN_THE_PAST)) != 0;
        final boolean isInOwnership = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_IS_IN_OWNERSHIP)) != 0;
        Place place = new Place(rowId, id, checkinsCount, usersCount, tipCount, name, category, type, level, owner, price, latitude, longitude);
        place.setIsAroundThePoint(isAroundThePoint);
        place.setIsAroundThePlayer(isAroundThePlayer);
        place.setIsInOwnership(isInOwnership);
        place.setIsVisitedInThePast(isVisitedInThePast);
        return place;
    }

    public Place(String id, long checkinsCount, long usersCount, long tipCount, String name, String category, String type, int level, long owner, long price, double latitude, double longitude) {
        mId = id;
        mCheckinsCount = checkinsCount;
        mUsersCount = usersCount;
        mTipCount = tipCount;
        mName = name;
        mCategory = category;
        mType = type;
        mLevel = level;
        mOwner = owner;
        mPrice = price;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Place(long rowId, String id, long checkinsCount, long usersCount, long tipCount, String name, String category, String type, int level, long owner, long price, double latitude, double longitude) {
        this(id, checkinsCount, usersCount, tipCount, name, category, type, level, owner, price, latitude, longitude);
        mRowId = rowId;
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

    public int getLevel() {
        return mLevel;
    }

    public long getOwner() {
        return mOwner;
    }

    public long getPrice() {
        return mPrice;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }


    public void setIsAroundThePoint(boolean isAroundThePoint) {
        mIsAroundThePoint = isAroundThePoint;
        mIsAroundThePointIsSet = true;
    }

    public void setIsAroundThePlayer(boolean isAroundThePlayer) {
        mIsAroundThePlayer = isAroundThePlayer;
        mIsAroundThePlayerIsSet = true;
    }

    public void setIsInOwnership(boolean isInOwnership) {
        mIsInOwnership = isInOwnership;
        mIsInOwnershipIsSet = true;
    }

    public void setIsVisitedInThePast(boolean isVisitedInThePast) {
        mIsVisitedInThePast = isVisitedInThePast;
        mIsVisitedInThePastIsSet = true;
    }

    public boolean getIsAroundThePointIsSet() {
        return mIsAroundThePointIsSet;
    }

    public boolean getIsAroundThePlayerIsSet() {
        return mIsAroundThePlayerIsSet;
    }

    public boolean getIsInOwnershipIsSet() {
        return mIsInOwnershipIsSet;
    }

    public boolean getIsVisitedInThePastIsSet() {
        return mIsVisitedInThePastIsSet;
    }

    public void writeToDatabase(Context context) {
        ContentValues values = new ContentValues();
        values.put(BuyPlacesContract.Places.COLUMN_ID, mId);
        values.put(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT, mCheckinsCount);
        values.put(BuyPlacesContract.Places.COLUMN_USERS_COUNT, mUsersCount);
        values.put(BuyPlacesContract.Places.COLUMN_TIP_COUNT, mTipCount);
        values.put(BuyPlacesContract.Places.COLUMN_NAME, mName);
        values.put(BuyPlacesContract.Places.COLUMN_CATEGORY, mCategory);
        values.put(BuyPlacesContract.Places.COLUMN_TYPE, mType);
        values.put(BuyPlacesContract.Places.COLUMN_LEVEL, mLevel);
        values.put(BuyPlacesContract.Places.COLUMN_OWNER, mOwner);
        values.put(BuyPlacesContract.Places.COLUMN_PRICE, mPrice);
        values.put(BuyPlacesContract.Places.COLUMN_LATITUDE, mLatitude);
        values.put(BuyPlacesContract.Places.COLUMN_LONGITUDE, mLongitude);
        if (mIsAroundThePointIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, mIsAroundThePoint);
        if (mIsAroundThePlayerIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, mIsAroundThePlayer);
        if (mIsInOwnershipIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_IN_OWNERSHIP, mIsInOwnership);
        if (mIsVisitedInThePastIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_VISITED_IN_THE_PAST, mIsVisitedInThePast);
        if (context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, values, BuyPlacesContract.Places.WITH_SPECIFIED_ID_SELECTION, new String[]{mId}) == 0)
            context.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
    }


}