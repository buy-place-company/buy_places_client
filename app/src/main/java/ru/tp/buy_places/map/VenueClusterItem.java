package ru.tp.buy_places.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import ru.tp.buy_places.service.resourses.Place;

/**
 * Created by Ivan on 05.05.2015.
 */
public class VenueClusterItem implements ClusterItem {

    private final Place mPlace;

    public VenueClusterItem(Place place) {
        mPlace = place;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
    }

    public String getName() {
        return mPlace.getName();
    }

    @Override
    public int hashCode() {
        return mPlace.getId().hashCode();
    }


    public Long getBuyPrice() {
        return mPlace.getBuyPrice();
    }

    public Long getSellPrice() { return mPlace.getSellPrice();}

    public long getRowId() {
        return mPlace.getRowId();
    }

    public Place getPlace(){
        return mPlace;
    }

    public long getCheckinsCount() {
        return mPlace.getCheckinsCount();
    }

    public int getLevel() {
        return mPlace.getLevel();
    }
}
