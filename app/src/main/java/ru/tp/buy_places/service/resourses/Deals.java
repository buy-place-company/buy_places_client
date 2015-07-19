package ru.tp.buy_places.service.resourses;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 17.07.2015.
 */
public class Deals implements Resource {
    private final List<Deal> mDeals;

    public Deals(List<Deal> deals) {
        mDeals = deals;
    }

    public static Deals fromCursor(Cursor cursor) {
        List<Deal> deals = new ArrayList<>();
        while (cursor.moveToNext()) {
            deals.add(Deal.fromCursor(cursor));
        }
        return new Deals(deals);
    }

    public List<Deal> getDeals() {
        return mDeals;
    }
}
