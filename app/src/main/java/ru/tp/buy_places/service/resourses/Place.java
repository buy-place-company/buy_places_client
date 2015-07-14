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
    private final String mId;
    private final long mCheckinsCount;
    private final long mUsersCount;
    private final long mTipCount;
    private final String mName;
    private final String mCategory;
    private final String mType;
    private final int mLevel;
    private final Player mOwner;
    private final long mBuyPrice;
    private final long mSellPrice;
    private final long mDealPrice;
    private final long mUpgradePrice;
    private final long mLoot;
    private final long mMaxLoot;
    private final long mIncome;
    private final long mExpense;
    private final double mLatitude;
    private final double mLongitude;
    private long mRowId;
    private boolean mIsAroundThePoint;
    private boolean mIsAroundThePlayer;
    private boolean mIsInOwnership;
    private boolean mIsVisitedInThePast;
    private boolean mStateUpdating;

    private boolean mIsAroundThePointIsSet = false;
    private boolean mIsAroundThePlayerIsSet = false;
    private boolean mIsInOwnershipIsSet = false;
    private boolean mIsVisitedInThePastIsSet = false;
    private boolean mStateUpdatingIsSet = false;

    private Place(String id, long checkinsCount, long usersCount, long tipCount, String name, String category, String type, int level, Player owner, long buyPrice, long sellPrice, long dealPrice, long upgradePrice, long loot, long maxLoot, long income, long expense, double latitude, double longitude) {
        mId = id;
        mCheckinsCount = checkinsCount;
        mUsersCount = usersCount;
        mTipCount = tipCount;
        mName = name;
        mCategory = category;
        mType = type;
        mLevel = level;
        mOwner = owner;
        mBuyPrice = buyPrice;
        mSellPrice = sellPrice;
        mDealPrice = dealPrice;
        mUpgradePrice = upgradePrice;
        mLoot = loot;
        mMaxLoot = maxLoot;
        mIncome = income;
        mExpense = expense;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    private Place(long rowId, String id, long checkinsCount, long usersCount, long tipCount, String name, String category, String type, int level, Player owner, long buyPrice, long sellPrice, long dealPrice, long upgradePrice, long loot, long maxLoot, long income, long expense, double latitude, double longitude) {
        this(id, checkinsCount, usersCount, tipCount, name, category, type, level, owner, buyPrice, sellPrice, dealPrice, upgradePrice, loot, maxLoot, income, expense, latitude, longitude);
        mRowId = rowId;
    }

    public static Place fromJSONObject(JSONObject placeData) {
        String id = placeData.optString("id");
        final JSONObject stats = placeData.optJSONObject("stats");
        long checkinsCount = stats.optLong("checkinsCount");
        long usersCount = stats.optLong("usersCount");
        long tipCount = stats.optLong("tipCount");
        String name = placeData.optString("name");
        String category = placeData.optString("category");
        String type = placeData.optString("type");
        int level = placeData.optInt("lvl");
        Player owner = placeData.isNull("owner") ? null : Player.fromJSONObject(placeData.optJSONObject("owner"));
        long buyPrice = placeData.optLong("buy_price");
        long sellPrice = placeData.optLong("sell_price");
        long dealPrice = placeData.optLong("deal_price");
        long upgradePrice = placeData.optLong("upgrade_price");
        long loot = placeData.optLong("loot");
        long maxLoot = placeData.optLong("max_loot");
        long income = placeData.optLong("income");
        long expense = placeData.optLong("expense");
        double latitude = placeData.optDouble("latitude");
        double longitude = placeData.optDouble("longitude");
        return new Place(id, checkinsCount, usersCount, tipCount, name, category, type, level, owner, buyPrice, sellPrice, dealPrice, upgradePrice, loot, maxLoot, income, expense, latitude, longitude);
    }

    public static Place fromCursor(Cursor row) {
        long rowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_ROW_ID));
        String id = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_ID));
        long checkinsCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_CHECKINS_COUNT));
        long usersCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_USERS_COUNT));
        long tipCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_TIP_COUNT));
        String name = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_NAME));
        String category = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_CATEGORY));
        String type = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_TYPE));
        int level = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LEVEL));
        Player owner;
        if (!row.isNull(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_ID))) {
            long ownersRowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_ROW_ID));
            long ownersId = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_ID));
            String ownersUsername = row.getString(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_USERNAME));
            int ownersLevel = row.getInt(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_LEVEL));
            String ownersAvatar = row.getString(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_AVATAR));
            long ownersScore = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_SCORE));
            int ownersPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_PLACES));
            int ownersMaxPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_MAX_PLACES));
            owner = new Player(ownersRowId, ownersId, ownersUsername, ownersLevel, ownersAvatar, ownersScore, ownersPlaces, ownersMaxPlaces);
        } else {
            owner = null;
        }
        long buyPrice = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_BUY_PRICE));
        long sellPrice = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_SELL_PRICE));
        long dealPrice = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_DEAL_PRICE));
        long upgradePrice = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_UPGRADE_PRICE));
        long loot = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LOOT));
        long maxLoot = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_MAX_LOOT));
        long income = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_INCOME));
        long expense = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_EXPENSE));
        double latitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LATITUDE));
        double longitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LONGITUDE));
        final boolean isAroundThePoint = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_AROUND_THE_POINT)) != 0;
        final boolean isAroundThePlayer = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_AROUND_THE_PLAYER)) != 0;
        final boolean isVisitedInThePast = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_VISITED_IN_THE_PAST)) != 0;
        final boolean isInOwnership = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_IN_OWNERSHIP)) != 0;
        final boolean stateUpdating = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_STATE_UPDATING)) != 0;
        Place place = new Place(rowId, id, checkinsCount, usersCount, tipCount, name, category, type, level, owner, buyPrice, sellPrice, dealPrice, upgradePrice, loot, maxLoot, income, expense, latitude, longitude);
        place.setIsAroundThePoint(isAroundThePoint);
        place.setIsAroundThePlayer(isAroundThePlayer);
        place.setIsInOwnership(isInOwnership);
        place.setIsVisitedInThePast(isVisitedInThePast);
        place.setStateUpdating(stateUpdating);
        return place;
    }

    public String getId() {
        return mId;
    }

    public long getRowId() {
        return mRowId;
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

    public long getExpense() {
        return mExpense;
    }

    public long getIncome() {
        return mIncome;
    }

    public int getLevel() {
        return mLevel;

    }

    public Player getOwner() {
        return mOwner;
    }

    public long getPrice() {
        return mBuyPrice;
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

    public void setStateUpdating(boolean stateUpdating) {
        mStateUpdating = stateUpdating;
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

    public boolean isInOwnership() {
        return mIsInOwnership;
    }

    public boolean getIsVisitedInThePastIsSet() {
        return mIsVisitedInThePastIsSet;
    }

    public void writeToDatabase(Context context) {
        Long ownersRowId = mOwner==null?null:mOwner.writeToDatabase(context);

        ContentValues values = new ContentValues();
        values.put(BuyPlacesContract.Places.COLUMN_ID, mId);
        values.put(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT, mCheckinsCount);
        values.put(BuyPlacesContract.Places.COLUMN_USERS_COUNT, mUsersCount);
        values.put(BuyPlacesContract.Places.COLUMN_TIP_COUNT, mTipCount);
        values.put(BuyPlacesContract.Places.COLUMN_NAME, mName);
        values.put(BuyPlacesContract.Places.COLUMN_CATEGORY, mCategory);
        values.put(BuyPlacesContract.Places.COLUMN_TYPE, mType);
        values.put(BuyPlacesContract.Places.COLUMN_LEVEL, mLevel);
        values.put(BuyPlacesContract.Places.COLUMN_BUY_PRICE, mBuyPrice);
        values.put(BuyPlacesContract.Places.COLUMN_SELL_PRICE, mSellPrice);
        values.put(BuyPlacesContract.Places.COLUMN_UPGRADE_PRICE, mUpgradePrice);
        values.put(BuyPlacesContract.Places.COLUMN_DEAL_PRICE, mDealPrice);
        values.put(BuyPlacesContract.Places.COLUMN_LOOT, mLoot);
        values.put(BuyPlacesContract.Places.COLUMN_MAX_LOOT, mMaxLoot);
        values.put(BuyPlacesContract.Places.COLUMN_EXPENSE, mExpense);
        values.put(BuyPlacesContract.Places.COLUMN_INCOME, mIncome);
        values.put(BuyPlacesContract.Places.COLUMN_LATITUDE, mLatitude);
        values.put(BuyPlacesContract.Places.COLUMN_LONGITUDE, mLongitude);
        values.put(BuyPlacesContract.Places.COLUMN_OWNER, ownersRowId);
        if (mIsAroundThePointIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_POINT, mIsAroundThePoint);
        if (mIsAroundThePlayerIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_AROUND_THE_PLAYER, mIsAroundThePlayer);
        if (mIsInOwnershipIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_IN_OWNERSHIP, mIsInOwnership);
        if (mIsVisitedInThePastIsSet)
            values.put(BuyPlacesContract.Places.COLUMN_IS_VISITED_IN_THE_PAST, mIsVisitedInThePast);

        values.put(BuyPlacesContract.Places.COLUMN_STATE_UPDATING, mStateUpdating);

        if (context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, values, BuyPlacesContract.Places.WITH_SPECIFIED_ID_SELECTION, new String[]{mId}) == 0)
            context.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);

    }
}