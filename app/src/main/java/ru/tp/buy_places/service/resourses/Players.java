package ru.tp.buy_places.service.resourses;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.content_provider.BuyPlacesContract;

/**
 * Created by Ivan on 20.07.2015.
 */
public class Players implements Resource {
    private final List<Player> mPlayers;

    public Players(List<Player> players) {
        mPlayers = players;
    }

    public static Players fromJsonArray(JSONArray usersJsonArray){
        if (usersJsonArray == null)
            return null;
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < usersJsonArray.length(); i++) {
            JSONObject userJsonObject = usersJsonArray.optJSONObject(i);
            Player user = Player.fromJSONObject(userJsonObject);
            players.add(user);
        }
        return new Players(players);
    }

    public void writeToDatabase(Context context, long offset) {
        for (int i = 0; i < mPlayers.size(); i++) {
            Player player = mPlayers.get(i);
            player.setPosition(offset + i + 1);
            player.writeToDatabase(context);
        }
        context.getContentResolver().notifyChange(BuyPlacesContract.Players.CONTENT_URI, null);
    }
}
