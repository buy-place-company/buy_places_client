package ru.tp.buy_places.content_provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ivan on 07.04.2015.
 */
public final class BuyPlacesContract {
    public static final String AUTHORITY = "ru.tp.buy_places.provider";
    public static final String SCHEME = "content://";
    public static final Uri AUTHORITY_URI = Uri.parse(SCHEME + AUTHORITY);


    private BuyPlacesContract() {

    }

    public static final class Places implements BaseColumns {
        public static final String TABLE_NAME = "places";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CHECKINS_COUNT = "checkins_count";
        public static final String COLUMN_USERS_COUNT = "users_count";
        public static final String COLUMN_TIP_COUNT = "tip_count";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_INCOME = "income";
        public static final String COLUMN_EXPENSE = "expense";
        public static final String COLUMN_MAX_LOOT = "max_loot";
        public static final String COLUMN_LOOT = "loot";
        public static final String COLUMN_BUY_PRICE = "buy_price";
        public static final String COLUMN_SELL_PRICE = "sell_price";
        public static final String COLUMN_DEAL_PRICE = "deal_price";
        public static final String COLUMN_UPGRADE_PRICE = "upgrade_price";
        public static final String COLUMN_IS_AROUND_THE_POINT = "is_around_the_point";
        public static final String COLUMN_IS_AROUND_THE_PLAYER = "is_around_the_player";
        public static final String COLUMN_IS_VISITED_IN_THE_PAST = "is_visited_in_the_past";
        public static final String COLUMN_IS_IN_OWNERSHIP = "is_in_ownership";
        public static final String COLUMN_STATE_UPDATING = "state_updating";

        public static final String COLUMN_FULL_ROW_ID = TABLE_NAME + "." + _ID;
        public static final String COLUMN_FULL_ID = TABLE_NAME + "." + COLUMN_ID;
        public static final String COLUMN_FULL_CHECKINS_COUNT = TABLE_NAME + "." + COLUMN_CHECKINS_COUNT;
        public static final String COLUMN_FULL_USERS_COUNT = TABLE_NAME + "." + COLUMN_USERS_COUNT;
        public static final String COLUMN_FULL_TIP_COUNT = TABLE_NAME + "." + COLUMN_TIP_COUNT;
        public static final String COLUMN_FULL_NAME = TABLE_NAME + "." + COLUMN_NAME;
        public static final String COLUMN_FULL_CATEGORY = TABLE_NAME + "." + COLUMN_CATEGORY;
        public static final String COLUMN_FULL_TYPE = TABLE_NAME + "." + COLUMN_TYPE;
        public static final String COLUMN_FULL_LEVEL = TABLE_NAME + "." + COLUMN_LEVEL;
        public static final String COLUMN_FULL_OWNER = TABLE_NAME + "." + COLUMN_OWNER;
        public static final String COLUMN_FULL_LATITUDE = TABLE_NAME + "." + COLUMN_LATITUDE;
        public static final String COLUMN_FULL_LONGITUDE = COLUMN_LONGITUDE;
        public static final String COLUMN_FULL_INCOME = TABLE_NAME + "." + COLUMN_INCOME;
        public static final String COLUMN_FULL_EXPENSE = TABLE_NAME + "." + COLUMN_EXPENSE;
        public static final String COLUMN_FULL_MAX_LOOT = TABLE_NAME + "." + COLUMN_MAX_LOOT;
        public static final String COLUMN_FULL_LOOT = TABLE_NAME + "." + COLUMN_LOOT;
        public static final String COLUMN_FULL_BUY_PRICE = TABLE_NAME + "." + COLUMN_BUY_PRICE;
        public static final String COLUMN_FULL_SELL_PRICE = TABLE_NAME + "." + COLUMN_SELL_PRICE;
        public static final String COLUMN_FULL_DEAL_PRICE = TABLE_NAME + "." + COLUMN_DEAL_PRICE;
        public static final String COLUMN_FULL_UPGRADE_PRICE = TABLE_NAME + "." + COLUMN_UPGRADE_PRICE;
        public static final String COLUMN_FULL_IS_AROUND_THE_POINT = TABLE_NAME + "." + COLUMN_IS_AROUND_THE_POINT;
        public static final String COLUMN_FULL_IS_AROUND_THE_PLAYER = TABLE_NAME + "." + COLUMN_IS_AROUND_THE_PLAYER;
        public static final String COLUMN_FULL_IS_VISITED_IN_THE_PAST = TABLE_NAME + "." + COLUMN_IS_VISITED_IN_THE_PAST;
        public static final String COLUMN_FULL_IS_IN_OWNERSHIP = TABLE_NAME + "." + COLUMN_IS_IN_OWNERSHIP;
        public static final String COLUMN_FULL_STATE_UPDATING = TABLE_NAME + "." + COLUMN_STATE_UPDATING;

        public static final String COLUMN_ALIAS_ROW_ID = "places_row_id";
        public static final String COLUMN_ALIAS_ID = "places_id";
        public static final String COLUMN_ALIAS_CHECKINS_COUNT = "places_checkins_count";
        public static final String COLUMN_ALIAS_USERS_COUNT = "places_users_count";
        public static final String COLUMN_ALIAS_TIP_COUNT = "places_tip_count";
        public static final String COLUMN_ALIAS_NAME = "places_name";
        public static final String COLUMN_ALIAS_CATEGORY = "places_category";
        public static final String COLUMN_ALIAS_TYPE = "places_type";
        public static final String COLUMN_ALIAS_LEVEL = "places_level";
        public static final String COLUMN_ALIAS_OWNER = "places_owner";
        public static final String COLUMN_ALIAS_LATITUDE = "places_latitude";
        public static final String COLUMN_ALIAS_LONGITUDE = "places_longitude";
        public static final String COLUMN_ALIAS_INCOME = "places_income";
        public static final String COLUMN_ALIAS_EXPENSE = "places_expense";
        public static final String COLUMN_ALIAS_MAX_LOOT = "places_max_loot";
        public static final String COLUMN_ALIAS_LOOT = "places_loot";
        public static final String COLUMN_ALIAS_BUY_PRICE = "places_buy_price";
        public static final String COLUMN_ALIAS_SELL_PRICE = "places_sell_price";
        public static final String COLUMN_ALIAS_DEAL_PRICE = "places_deal_price";
        public static final String COLUMN_ALIAS_UPGRADE_PRICE = "places_upgrade_price";
        public static final String COLUMN_ALIAS_IS_AROUND_THE_POINT = "places_is_around_the_point";
        public static final String COLUMN_ALIAS_IS_AROUND_THE_PLAYER = "places_is_around_the_player";
        public static final String COLUMN_ALIAS_IS_VISITED_IN_THE_PAST = "places_is_visited_in_the_past";
        public static final String COLUMN_ALIAS_IS_IN_OWNERSHIP = "places_is_in_ownership";
        public static final String COLUMN_ALIAS_STATE_UPDATING = "places_state_updating";



        public static final String[] ALL_COLUMNS_PROJECTION = {
                _ID,
                COLUMN_ID,
                COLUMN_CHECKINS_COUNT,
                COLUMN_USERS_COUNT,
                COLUMN_TIP_COUNT,
                COLUMN_NAME,
                COLUMN_CATEGORY,
                COLUMN_TYPE,
                COLUMN_LEVEL,
                COLUMN_OWNER,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_INCOME,
                COLUMN_EXPENSE,
                COLUMN_MAX_LOOT,
                COLUMN_LOOT,
                COLUMN_BUY_PRICE,
                COLUMN_SELL_PRICE,
                COLUMN_DEAL_PRICE,
                COLUMN_UPGRADE_PRICE,
                COLUMN_IS_AROUND_THE_POINT,
                COLUMN_IS_AROUND_THE_PLAYER,
                COLUMN_IS_VISITED_IN_THE_PAST,
                COLUMN_IS_IN_OWNERSHIP,
                COLUMN_STATE_UPDATING};

        static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                COLUMN_ID + " TEXT NOT NULL UNIQUE" + "," +
                COLUMN_CHECKINS_COUNT + " INTEGER NOT NULL" + "," +
                COLUMN_USERS_COUNT + " INTEGER" + "," +
                COLUMN_TIP_COUNT + " INTEGER" + "," +
                COLUMN_NAME + " TEXT" + "," +
                COLUMN_CATEGORY + " TEXT" + "," +
                COLUMN_TYPE + " TEXT" + "," +
                COLUMN_LEVEL + " INTEGER" + "," +
                COLUMN_OWNER + " INTEGER" + "," +
                COLUMN_LATITUDE + " REAL" + "," +
                COLUMN_LONGITUDE + " REAL" + "," +
                COLUMN_INCOME + " INTEGER" + "," +
                COLUMN_EXPENSE + " INTEGER" + "," +
                COLUMN_MAX_LOOT + " INTEGER" + "," +
                COLUMN_LOOT + " INTEGER" + "," +
                COLUMN_BUY_PRICE + " INTEGER" + "," +
                COLUMN_SELL_PRICE + " INTEGER" + "," +
                COLUMN_DEAL_PRICE + " INTEGER" + "," +
                COLUMN_UPGRADE_PRICE + " INTEGER" + "," +
                COLUMN_IS_AROUND_THE_POINT + " INTEGER DEFAULT 0" + "," +
                COLUMN_IS_AROUND_THE_PLAYER + " INTEGER DEFAULT 0" + "," +
                COLUMN_IS_VISITED_IN_THE_PAST + " INTEGER DEFAULT 0" + "," +
                COLUMN_IS_IN_OWNERSHIP + " INTEGER DEFAULT 0" + "," +
                COLUMN_STATE_UPDATING + " INTEGER DEFAULT 0" + "," +
                "FOREIGN KEY" + "(" + COLUMN_OWNER + ")" + " REFERENCES " + Players.TABLE_NAME + "(" + Players._ID + ")" +
                ");";

        public static final String[] WITH_OWNERS_COLUMNS_PROJECTION = {
                Places.COLUMN_FULL_ROW_ID + " AS " +  Places.COLUMN_ALIAS_ROW_ID,
                Places.COLUMN_FULL_ID + " AS " + Places.COLUMN_ALIAS_ID,
                Places.COLUMN_FULL_CHECKINS_COUNT + " AS " + Places.COLUMN_ALIAS_CHECKINS_COUNT,
                Places.COLUMN_FULL_USERS_COUNT + " AS " + Places.COLUMN_ALIAS_USERS_COUNT,
                Places.COLUMN_FULL_TIP_COUNT + " AS " + Places.COLUMN_ALIAS_TIP_COUNT,
                Places.COLUMN_FULL_NAME + " AS " + Places.COLUMN_ALIAS_NAME,
                Places.COLUMN_FULL_CATEGORY + " AS " + Places.COLUMN_ALIAS_CATEGORY,
                Places.COLUMN_FULL_TYPE + " AS " + Places.COLUMN_ALIAS_TYPE,
                Places.COLUMN_FULL_LEVEL + " AS " + Places.COLUMN_ALIAS_LEVEL,
                Places.COLUMN_FULL_LATITUDE + " AS " + Places.COLUMN_ALIAS_LATITUDE,
                Places.COLUMN_FULL_LONGITUDE + " AS " + Places.COLUMN_ALIAS_LONGITUDE,
                Places.COLUMN_FULL_INCOME + " AS " + Places.COLUMN_ALIAS_INCOME,
                Places.COLUMN_FULL_EXPENSE + " AS " + Places.COLUMN_ALIAS_EXPENSE,
                Places.COLUMN_FULL_MAX_LOOT + " AS " + Places.COLUMN_ALIAS_MAX_LOOT,
                Places.COLUMN_FULL_LOOT + " AS " + Places.COLUMN_ALIAS_LOOT,
                Places.COLUMN_FULL_BUY_PRICE + " AS " + Places.COLUMN_ALIAS_BUY_PRICE,
                Places.COLUMN_FULL_SELL_PRICE + " AS " + Places.COLUMN_ALIAS_SELL_PRICE,
                Places.COLUMN_FULL_DEAL_PRICE + " AS " + Places.COLUMN_ALIAS_DEAL_PRICE,
                Places.COLUMN_FULL_UPGRADE_PRICE + " AS " + Places.COLUMN_ALIAS_UPGRADE_PRICE,
                Places.COLUMN_FULL_IS_AROUND_THE_POINT + " AS " + Places.COLUMN_ALIAS_IS_AROUND_THE_POINT,
                Places.COLUMN_FULL_IS_AROUND_THE_PLAYER + " AS " + Places.COLUMN_ALIAS_IS_AROUND_THE_PLAYER,
                Places.COLUMN_FULL_IS_VISITED_IN_THE_PAST + " AS " + Places.COLUMN_ALIAS_IS_VISITED_IN_THE_PAST,
                Places.COLUMN_FULL_IS_IN_OWNERSHIP + " AS " + Places.COLUMN_ALIAS_IS_IN_OWNERSHIP,
                Places.COLUMN_FULL_STATE_UPDATING + " AS " + Places.COLUMN_ALIAS_STATE_UPDATING,
                Players.COLUMN_FULL_ROW_ID + " AS " + Players.COLUMN_ALIAS_ROW_ID,
                Players.COLUMN_FULL_ID + " AS " + Players.COLUMN_ALIAS_ID,
                Players.COLUMN_FULL_USERNAME + " AS " + Players.COLUMN_ALIAS_USERNAME,
                Players.COLUMN_FULL_LEVEL + " AS " + Players.COLUMN_ALIAS_LEVEL,
                Players.COLUMN_FULL_AVATAR + " AS " + Players.COLUMN_ALIAS_AVATAR,
                Players.COLUMN_FULL_SCORE + " AS " + Players.COLUMN_ALIAS_SCORE,
                Players.COLUMN_FULL_PLACES + " AS " + Players.COLUMN_ALIAS_PLACES,
                Players.COLUMN_FULL_MAX_PLACES + " AS " + Players.COLUMN_ALIAS_MAX_PLACES
        };


        static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String WITH_OWNERS_TABLE_NAME = Places.TABLE_NAME + " INNER JOIN " + Players.TABLE_NAME + " ON " + "(" + Places.COLUMN_FULL_OWNER + "=" + Players.COLUMN_FULL_ROW_ID + ")";
        public static final String ONLY_AROUND_THE_POINT_SELECTION =
                COLUMN_IS_AROUND_THE_POINT + "=1 AND " +
                        COLUMN_IS_AROUND_THE_PLAYER + "=0 AND " +
                        COLUMN_IS_VISITED_IN_THE_PAST + "=0";
        public static final String WITH_SPECIFIED_ROW_ID_SELECTION =TABLE_NAME + "." + _ID + "=?";
        public static final String WITH_SPECIFIED_ID_SELECTION = COLUMN_ID + "=?";
        public static final String AROUND_THE_POINT_SELECTION = COLUMN_IS_AROUND_THE_POINT + "=1";
        public static final String AROUND_THE_PLAYER_SELECTION = COLUMN_IS_AROUND_THE_PLAYER + "=1";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.buy_places." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.buy_places." + TABLE_NAME;
    }

    public static final class Players implements BaseColumns {
        public static final String TABLE_NAME = "players";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_PLACES = "places";
        public static final String COLUMN_MAX_PLACES = "max_places";

        public static final String COLUMN_FULL_ROW_ID = TABLE_NAME + "." + _ID;
        public static final String COLUMN_FULL_ID = TABLE_NAME + "." + COLUMN_ID;
        public static final String COLUMN_FULL_USERNAME = TABLE_NAME + "." + COLUMN_USERNAME;
        public static final String COLUMN_FULL_LEVEL = TABLE_NAME + "." + COLUMN_LEVEL;
        public static final String COLUMN_FULL_AVATAR = TABLE_NAME + "." + COLUMN_AVATAR;
        public static final String COLUMN_FULL_SCORE = TABLE_NAME + "." + COLUMN_SCORE;
        public static final String COLUMN_FULL_PLACES = TABLE_NAME + "." + COLUMN_PLACES;
        public static final String COLUMN_FULL_MAX_PLACES = TABLE_NAME + "." + COLUMN_MAX_PLACES;

        public static final String COLUMN_ALIAS_ROW_ID = "players_row_id";
        public static final String COLUMN_ALIAS_ID = "players_id";
        public static final String COLUMN_ALIAS_USERNAME = "players_username";
        public static final String COLUMN_ALIAS_LEVEL = "players_level";
        public static final String COLUMN_ALIAS_AVATAR = "players_avatar";
        public static final String COLUMN_ALIAS_SCORE = "players_score";
        public static final String COLUMN_ALIAS_PLACES = "players_places";
        public static final String COLUMN_ALIAS_MAX_PLACES = "players_max_places";

        public static final String [] ALL_COLUMNS_PROJECTION = {
                _ID,
                COLUMN_ID,
                COLUMN_USERNAME,
                COLUMN_LEVEL,
                COLUMN_AVATAR,
                COLUMN_SCORE,
                COLUMN_PLACES,
                COLUMN_MAX_PLACES
        };

        static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                COLUMN_ID + " INTEGER NOT NULL UNIQUE" + "," +
                COLUMN_USERNAME + " TEXT" + "," +
                COLUMN_LEVEL + " INTEGER" + "," +
                COLUMN_AVATAR + " TEXT" + "," +
                COLUMN_SCORE + " INTEGER" + "," +
                COLUMN_PLACES + " INTEGER" + "," +
                COLUMN_MAX_PLACES + " INTEGER" + ");";

        static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String WITH_SPECIFIED_ID_SELECTION = COLUMN_ID + "=?";
        public static final String WITH_SPECIFIED_ROW_ID_SELECTION = _ID + "=?";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.buy_places." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.buy_places." + TABLE_NAME;
    }

    public static final class Deals implements BaseColumns {
        public static final String TABLE_NAME = "deals";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PLAYER = "player";
        public static final String COLUMN_PLACE = "place";

        public static final String [] ALL_COLUMNS_PROJECTION = {
                _ID,
                COLUMN_ID,
                COLUMN_TYPE,
                COLUMN_PLAYER,
                COLUMN_PLACE
        };

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                COLUMN_ID + " INTEGER NOT NULL UNIQUE" + "," +
                COLUMN_TYPE + " TEXT" + "," +
                COLUMN_PLAYER + " INTEGER" + "," +
                COLUMN_PLACE + " INTEGER" + "," +
                "FOREIGN KEY" + "(" + COLUMN_PLAYER + ")" + " REFERENCES " + Players.TABLE_NAME + "(" + Players._ID + ")" + "," +
                "FOREIGN KEY" + "(" + COLUMN_PLACE + ")" + " REFERENCES " + Places.TABLE_NAME + "(" + Places._ID + ")" +
                ");";

        static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}