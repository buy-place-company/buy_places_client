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
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_IS_AROUND_THE_POINT = "is_around_the_point";
        public static final String COLUMN_IS_AROUND_THE_PLAYER = "is_around_the_player";
        public static final String COLUMN_IS_VISITED_IN_THE_PAST = "is_visited_in_the_past";
        public static final String COLUMN_IS_IN_OWNERSHIP = "is_in_ownership";
        public static final String COLUMN_STATE_UPDATING = "state_updating";

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
                COLUMN_PRICE,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
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
                COLUMN_PRICE + " INTEGER" + "," +
                COLUMN_LATITUDE + " REAL" + "," +
                COLUMN_LONGITUDE + " REAL" + "," +
                COLUMN_IS_AROUND_THE_POINT + " INTEGER DEFAULT 0" + "," +
                COLUMN_IS_AROUND_THE_PLAYER + " INTEGER DEFAULT 0" + "," +
                COLUMN_IS_VISITED_IN_THE_PAST + " INTEGER DEFAULT 0" + "," +
                COLUMN_IS_IN_OWNERSHIP + " INTEGER DEFAULT 0" + "," +
                COLUMN_STATE_UPDATING + " INTEGER DEFAULT 0" + ");";

        static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;


        public static final String ONLY_AROUND_THE_POINT_SELECTION =
                COLUMN_IS_AROUND_THE_POINT + "=1 AND " +
                        COLUMN_IS_AROUND_THE_PLAYER + "=0 AND " +
                        COLUMN_IS_VISITED_IN_THE_PAST + "=0";

        public static final String AROUND_THE_POINT_SELECTION = COLUMN_IS_AROUND_THE_POINT + "=1";
        public static final String AROUND_THE_PLAYER_SELECTION = COLUMN_IS_AROUND_THE_PLAYER + "=1";


        public static final String WITH_SPECIFIED_ID_SELECTION = COLUMN_ID + "=?";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.buy_places." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.buy_places." + TABLE_NAME;
    }
}