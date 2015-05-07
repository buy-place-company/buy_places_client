package ru.tp.buy_places.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Ivan on 05.05.2015.
 */
public class ObjectItem implements ClusterItem {

    private final long mRowId;
    private final String mId;
    private final String mName;
    private final double mLatitude;
    private final double mLongitude;

    public ObjectItem(long rowId, String id, String name, double latitude, double longitude) {
        mRowId = rowId;
        mId = id;
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(mLatitude, mLongitude);
    }

    public String getName() {
        return mName;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }
}
