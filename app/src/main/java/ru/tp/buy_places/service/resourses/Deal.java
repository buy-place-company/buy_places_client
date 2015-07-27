package ru.tp.buy_places.service.resourses;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONObject;

import ru.tp.buy_places.content_provider.BuyPlacesContract;

/**
 * Created by Ivan on 16.07.2015.
 */
public class Deal implements Resource{
    private long mRowId;
    private long mId;
    private final Player mPlayerFrom;
    private final Player mPlayerTo;
    private final String mDateExpired;
    private final String mDateAdded;

    public DealState getStatus() {
        return mStatus;
    }

    public long getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }

    public String getDateExpired() {
        return mDateExpired;
    }

    public String getDateAdded() {
        return mDateAdded;
    }

    public Place getVenue() {
        return mVenue;
    }

    private final DealState mStatus;
    private final long mAmount;
    private final String mType;
    private final Place mVenue;

    public Deal(long id, Player playerFrom, Player playerTo, String dateExpired, String dateAdded, DealState status, long amount, String type, Place venue) {
        mPlayerFrom = playerFrom;
        mPlayerTo = playerTo;
        mDateExpired = dateExpired;
        mDateAdded = dateAdded;
        mStatus = status;
        mAmount = amount;
        mType = type;
        mVenue = venue;
        mId = id;
    }

    public Deal(long rowId, long id, Player playerFrom, Player playerTo, String dateExpired, String dateAdded, DealState status, long amount, String type, Place venue) {
        this(id, playerFrom, playerTo, dateExpired, dateAdded, status, amount, type, venue);
        mRowId = rowId;
    }

    public static Deal fromJSONObject(JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        final JSONObject playerFromJSONObject = jsonObject.optJSONObject("user_from");
        final JSONObject playerToJsonObject = jsonObject.optJSONObject("user_to");
        final JSONObject venueJSONObject = jsonObject.optJSONObject("venue");
        final Player playerFrom = Player.fromJSONObject(playerFromJSONObject);
        final Player playerTo = Player.fromJSONObject(playerToJsonObject);
        final long id = jsonObject.optLong("id");
        final String dateExpired = jsonObject.optString("date_expired");
        final String dateAdded = jsonObject.optString("date_added");
        final DealState status = DealState.valueOf(jsonObject.optString("status").toUpperCase());
        final long amount = jsonObject.optLong("amount");
        final String type = jsonObject.optString("type");
        final Place venue = Place.fromJSONObject(venueJSONObject);
        return new Deal(id, playerFrom, playerTo, dateExpired, dateAdded, status, amount, type, venue);
    }


    public static Deal fromCursor(Cursor row) {
        long rowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_ROW_ID));
        long id = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_ID));

        long playerFromRowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_ROW_ID));
        long playerFromId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_ID));
        String playerFromUsername = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_USERNAME));
        int playerFromLevel = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_LEVEL));
        String playerFormAvatar = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_AVATAR));
        long playerFromCash = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_CASH));
        long playerFromScore = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_SCORE));
        int playerFromVenuesCount = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_PLACES));
        int playerFromMaxPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_FROM_MAX_PLACES));
        Player playerTo;
        if (!row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_ROW_ID))) {
            long playerToRowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_ROW_ID));
            long playerToId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_ID));
            String playerToUsername = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_USERNAME));
            int playerToLevel = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_LEVEL));
            String playerToAvatar = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_AVATAR));
            long playerToCash = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_CASH));
            long playerToScore = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_SCORE));
            int playerToVenuesCount = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_PLACES));
            int playerToMaxPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_PLAYER_TO_MAX_PLACES));
            playerTo = new Player(playerToRowId, playerToId, playerToUsername, playerToLevel, playerToAvatar, playerToCash, playerToScore, playerToVenuesCount, playerToMaxPlaces);
        } else {
            playerTo = null;
        }
        long venueOwnerRowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_ROW_ID));
        long venueOwnerId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_ID));
        String venueOwnerUsername = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_USERNAME));
        int venueOwnerLevel = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_LEVEL));
        String venueOwnerAvatar = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_AVATAR));
        long venueOwnerCash = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_CASH));
        long venueOwnerScore = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_SCORE));
        int venueOwnerVenuesCount = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_PLACES));
        int venueOwnerMaxPlaces = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_OWNER_MAX_PLACES));

        long venueRowId = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_ROW_ID));
        String venueId = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_ID));
        long venueCheckinsCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_CHECKINS_COUNT));
        long venueUsersCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_USERS_COUNT));
        long venueTipCount = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_TIP_COUNT));
        String venueName = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_NAME));
        String venueCategory = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_CATEGORY));

        Player venueOwner = new Player(venueOwnerRowId, venueOwnerId, venueOwnerUsername, venueOwnerLevel, venueOwnerAvatar, venueOwnerCash, venueOwnerScore, venueOwnerVenuesCount, venueOwnerMaxPlaces);
        Player playerFrom = new Player(playerFromRowId, playerFromId, playerFromUsername, playerFromLevel, playerFormAvatar, playerFromCash, playerFromScore, playerFromVenuesCount, playerFromMaxPlaces);

        int venueLevel = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_LEVEL));
        Long venueBuyPrice = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_BUY_PRICE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_BUY_PRICE)):null;
        Long venueSellPrice = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_SELL_PRICE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_SELL_PRICE)):null;
        Long venueUpgradePrice = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_UPGRADE_PRICE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_UPGRADE_PRICE)):null;
        Long venueLoot = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_LOOT))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_LOOT)):null;
        Long venueMaxLoot = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_MAX_LOOT))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_MAX_LOOT)):null;
        Long venueIncome = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_INCOME))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_INCOME)):null;
        Long venueExpense = !row.isNull(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_EXPENSE))?row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_EXPENSE)):null;
        double venueLatitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_LATITUDE));
        double venueLongitude = row.getDouble(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_LONGITUDE));
        boolean isFavourite = row.getInt(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_VENUE_IS_FAVOURITE)) != 0;
        Place venue = new Place(venueRowId, venueId, venueCheckinsCount, venueUsersCount, venueTipCount, venueName, venueCategory, venueLevel, venueOwner,venueBuyPrice, venueSellPrice, venueUpgradePrice, venueLoot, venueMaxLoot, venueIncome, venueExpense, venueLatitude, venueLongitude, isFavourite);


        String dateExpired = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_DATE_EXPIRED));
        String dateAdded = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_DATE_ADDED));
        DealState status = DealState.valueOf(row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_STATUS)).toUpperCase());
        long amount = row.getLong(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_AMOUNT));
        String type = row.getString(row.getColumnIndex(BuyPlacesContract.Deals.COLUMN_ALIAS_TYPE));
        return new Deal(rowId, id, playerFrom, playerTo, dateExpired, dateAdded, status, amount, type, venue);
    }


    public long writeToDatabase(Context context) {
        Long venueRowId = mVenue.writeToDatabase(context);
        Long playerToRowId = mPlayerTo != null ? mPlayerTo.writeToDatabase(context) : null;
        Long playerFromRowId = mPlayerFrom != null ? mPlayerFrom.writeToDatabase(context): null;

        ContentValues contentValues = new ContentValues();
        contentValues.put(BuyPlacesContract.Deals.COLUMN_ID, mId);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_PLAYER_FROM, playerFromRowId);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_PLAYER_TO, playerToRowId);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_DATE_EXPIRED, mDateExpired);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_DATE_ADDED, mDateAdded);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_STATUS, mStatus.toString());
        contentValues.put(BuyPlacesContract.Deals.COLUMN_AMOUNT, mAmount);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_TYPE, mType);
        contentValues.put(BuyPlacesContract.Deals.COLUMN_VENUE, venueRowId);
        long id;

        if (context.getContentResolver().update(BuyPlacesContract.Deals.CONTENT_URI, contentValues, BuyPlacesContract.Deals.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(mId)}) == 0) {
            Uri result = context.getContentResolver().insert(BuyPlacesContract.Deals.CONTENT_URI, contentValues);
            id = ContentUris.parseId(result);
        } else {
            Cursor cursor = context.getContentResolver().query(BuyPlacesContract.Deals.CONTENT_URI, BuyPlacesContract.Deals.COLUMN_ID_PROJECTION, BuyPlacesContract.Deals.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(mId)}, null);
            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Deals._ID));
            cursor.close();
        }
        return id;
    }


    public Player getPlayerFrom() {
        return mPlayerFrom;
    }

    public Player getPlayerTo() {
        return mPlayerTo;
    }

    public DealState getState() {
        return mStatus;
    }

    public long getRowId() {
        return mRowId;
    }

    public long getId() {
        return mId;
    }

    public void setRowId(long rowId) {
        mRowId = rowId;
    }

    public enum DealType {
        INCOMING,
        OUTGOING,
        ILLEGAL_STATE
    }

    public enum DealState {
        COMPLETED,
        UNCOMPLETED,
        REJECTED,
        REVOKED,
        ILLEGAL_STATE
    }
}
