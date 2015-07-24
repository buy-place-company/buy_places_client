package ru.tp.buy_places.service.resourses;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONObject;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.utils.AccountManagerHelper;

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
    private final int mLevel;
    private final Player mOwner;
    private final Long mBuyPrice;
    private final Long mSellPrice;
    private final Long mUpgradePrice;
    private final Long mLoot;
    private final Long mMaxLoot;
    private final Long mIncome;
    private final Long mExpense;
    private final double mLatitude;
    private final double mLongitude;
    private long mRowId;
    private boolean mIsAroundThePoint;
    private boolean mIsAroundThePlayer;
    private boolean mIsInOwnership;
    private boolean mStateUpdating;

    private boolean mIsAroundThePointIsSet = false;
    private boolean mIsAroundThePlayerIsSet = false;
    private boolean mIsInOwnershipIsSet = false;
    private boolean mStateUpdatingIsSet = false;

    private Place(String id, long checkinsCount, long usersCount, long tipCount, String name, String category, int level, Player owner, Long buyPrice, Long sellPrice, Long upgradePrice, Long loot, Long maxLoot, Long income, Long expense, double latitude, double longitude) {
        mId = id;
        mCheckinsCount = checkinsCount;
        mUsersCount = usersCount;
        mTipCount = tipCount;
        mName = name;
        mCategory = category;
        mLevel = level;
        mOwner = owner;
        mBuyPrice = buyPrice;
        mSellPrice = sellPrice;
        mUpgradePrice = upgradePrice;
        mLoot = loot;
        mMaxLoot = maxLoot;
        mIncome = income;
        mExpense = expense;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Place(long rowId, String id, long checkinsCount, long usersCount, long tipCount, String name, String category, int level, Player owner, Long buyPrice, Long sellPrice, Long upgradePrice, Long loot, Long maxLoot, Long income, Long expense, double latitude, double longitude) {
        this(id, checkinsCount, usersCount, tipCount, name, category, level, owner, buyPrice, sellPrice, upgradePrice, loot, maxLoot, income, expense, latitude, longitude);
        mRowId = rowId;
    }

    public static Place fromJSONObject(JSONObject placeData) {
        if (placeData == null)
            return null;
        String id = placeData.optString("id");
        final JSONObject stats = placeData.optJSONObject("stats");
        long checkinsCount = stats.optLong("checkinsCount");
        long usersCount = stats.optLong("usersCount");
        long tipCount = stats.optLong("tipCount");
        String name = placeData.optString("name");
        String category = placeData.optString("category");
        int level = placeData.optInt("lvl");
        double latitude = placeData.optDouble("latitude");
        double longitude = placeData.optDouble("longitude");
        Player owner = placeData.isNull("owner") ? null : Player.fromJSONObject(placeData.optJSONObject("owner"));
        Long buyPrice = placeData.has("buy_price")?placeData.optLong("buy_price"):null;

        Long sellPrice = placeData.has("sell_price")?placeData.optLong("sell_price"):null;
        Long upgradePrice = placeData.has("upgrade_price")?placeData.optLong("upgrade_price"):null;
        Long loot = placeData.has("loot")?placeData.optLong("loot"):null;
        Long maxLoot = placeData.has("max_loot")?placeData.optLong("max_loot"):null;
        Long income = placeData.has("income")?placeData.optLong("income"):null;
        Long expense = placeData.has("consumption")?placeData.optLong("consumption"):null;
        return new Place(id, checkinsCount, usersCount, tipCount, name, category, level, owner, buyPrice, sellPrice, upgradePrice, loot, maxLoot, income, expense, latitude, longitude);
    }

    public static Place fromCursor(Cursor row) {
        long rowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_ROW_ID));
        String id = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_ID));
        long checkinsCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_CHECKINS_COUNT));
        long usersCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_USERS_COUNT));
        long tipCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_TIP_COUNT));
        String name = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_NAME));
        String category = row.getString(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_CATEGORY));
        int level = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LEVEL));
        Player owner;
        if (!row.isNull(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_ID))) {
            long ownersRowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_ROW_ID));
            long ownersId = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_ID));
            String ownersUsername = row.getString(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_USERNAME));
            int ownersLevel = row.getInt(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_LEVEL));
            String ownersAvatar = row.getString(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_AVATAR));
            long ownersCash = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_CASH));
            long ownersScore = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_SCORE));
            int ownersPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_PLACES));
            int ownersMaxPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_MAX_PLACES));
            owner = new Player(ownersRowId, ownersId, ownersUsername, ownersLevel, ownersAvatar, ownersCash, ownersScore, ownersPlaces, ownersMaxPlaces);
            if (!row.isNull(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_ALIAS_POSITION))) {
                long position = row.getLong(row.getColumnIndex(BuyPlacesContract.Players.COLUMN_POSITION));
                owner.setPosition(position);
            }
        } else {
            owner = null;
        }
        Long buyPrice = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_BUY_PRICE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_BUY_PRICE)):null;
        Long sellPrice = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_SELL_PRICE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_SELL_PRICE)):null;
        Long upgradePrice = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_UPGRADE_PRICE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_UPGRADE_PRICE)):null;
        Long loot = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LOOT))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LOOT)):null;
        Long maxLoot = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_MAX_LOOT))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_MAX_LOOT)):null;
        Long income = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_INCOME))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_INCOME)):null;
        Long expense = !row.isNull(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_EXPENSE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_EXPENSE)):null;
        double latitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LATITUDE));
        double longitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_LONGITUDE));
        final boolean isAroundThePoint = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_AROUND_THE_POINT)) != 0;
        final boolean isAroundThePlayer = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_AROUND_THE_PLAYER)) != 0;
        final boolean isInOwnership = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_IS_IN_OWNERSHIP)) != 0;
        final boolean stateUpdating = row.getInt(row.getColumnIndex(BuyPlacesContract.Places.COLUMN_ALIAS_STATE_UPDATING)) != 0;
        Place place = new Place(rowId, id, checkinsCount, usersCount, tipCount, name, category, level, owner, buyPrice, sellPrice, upgradePrice, loot, maxLoot, income, expense, latitude, longitude);
        place.setIsAroundThePoint(isAroundThePoint);
        place.setIsAroundThePlayer(isAroundThePlayer);
        place.setIsInOwnership(isInOwnership);
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

    public Long getExpense() {
        return mExpense;
    }

    public Long getIncome() {
        return mIncome;
    }

    public int getLevel() {
        return mLevel;

    }

    public static boolean checkIsInOwnership(Context context, Player player) {
        // TODO Request Account Manager player's id and compare with parameter
        return player != null && player.getId() == AccountManagerHelper.getPlayerId(context);
    }

    public Player getOwner() {
        return mOwner;
    }

    public Long getBuyPrice() {
        return mBuyPrice;
    }

    public Long getSellPrice() {
        return mSellPrice;
    }

    public Long getUpgradePrice() {
        return mUpgradePrice;
    }

    public Long getLoot() {
        return mLoot;
    }

    public Long getMaxLoot() {
        return mMaxLoot;
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

    public long writeToDatabase(Context context) {
        Long ownersRowId = mOwner==null?null:mOwner.writeToDatabase(context);

        ContentValues values = new ContentValues();
        values.put(BuyPlacesContract.Places.COLUMN_ID, mId);
        values.put(BuyPlacesContract.Places.COLUMN_CHECKINS_COUNT, mCheckinsCount);
        values.put(BuyPlacesContract.Places.COLUMN_USERS_COUNT, mUsersCount);
        values.put(BuyPlacesContract.Places.COLUMN_TIP_COUNT, mTipCount);
        values.put(BuyPlacesContract.Places.COLUMN_NAME, mName);
        values.put(BuyPlacesContract.Places.COLUMN_CATEGORY, mCategory);
        values.put(BuyPlacesContract.Places.COLUMN_LEVEL, mLevel);
        values.put(BuyPlacesContract.Places.COLUMN_BUY_PRICE, mBuyPrice);
        values.put(BuyPlacesContract.Places.COLUMN_SELL_PRICE, mSellPrice);
        values.put(BuyPlacesContract.Places.COLUMN_UPGRADE_PRICE, mUpgradePrice);
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

        values.put(BuyPlacesContract.Places.COLUMN_STATE_UPDATING, mStateUpdating);
        long id;

        if (context.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, values, BuyPlacesContract.Places.WITH_SPECIFIED_ID_SELECTION, new String[]{mId}) == 0) {
            Uri result = context.getContentResolver().insert(BuyPlacesContract.Places.CONTENT_URI, values);
            id = ContentUris.parseId(result);
        } else {
            Cursor cursor = context.getContentResolver().query(BuyPlacesContract.Places.CONTENT_URI, BuyPlacesContract.Places.ALL_COLUMNS_PROJECTION, BuyPlacesContract.Places.WITH_SPECIFIED_ID_SELECTION, new String[]{mId}, null);
            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Places._ID));
            cursor.close();
        }
        return id;
    }
}