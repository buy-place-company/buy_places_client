package ru.tp.buy_places.service.resourses;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 16.07.2015.
 */
public class DealsFromServer implements Resource {
    private final List<Deal> mIncomingDeals;
    private final List<Deal> mOutgoingDeals;

    public DealsFromServer(List<Deal> incomingDeals, List<Deal> outgoingDeals) {
        mIncomingDeals = incomingDeals;
        mOutgoingDeals = outgoingDeals;
    }


    public void writeToDatabase(Context context) {
        for (Deal deal: mIncomingDeals) {
            deal.writeToDatabase(context);
        }
        for (Deal deal: mOutgoingDeals) {
            deal.writeToDatabase(context);
        }
    }

    public static DealsFromServer fromJsonObject(JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        List<Deal> outgoingDeals = new ArrayList<>();
        List<Deal> incomingDeals = new ArrayList<>();
        JSONArray outgoingDealsJsonArray = jsonObject.optJSONArray("outgoing");
        JSONArray incomingDealsJsonArray = jsonObject.optJSONArray("incoming");
        for (int i=0; i<outgoingDealsJsonArray.length();i++) {
            JSONObject dealJsonObject = outgoingDealsJsonArray.optJSONObject(i);
            outgoingDeals.add(Deal.fromJSONObject(dealJsonObject));
        }
        for (int i=0; i<incomingDealsJsonArray.length(); i++) {
            JSONObject dealJsonObject = incomingDealsJsonArray.optJSONObject(i);
            incomingDeals.add(Deal.fromJSONObject(dealJsonObject));
        }
        return new DealsFromServer(incomingDeals, outgoingDeals);
    }
}
