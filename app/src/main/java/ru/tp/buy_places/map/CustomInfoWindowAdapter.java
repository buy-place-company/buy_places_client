package ru.tp.buy_places.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.R;

/**
 * Created by home on 11.05.2015.
 */
public class CustomInfoWindowAdapter implements  GoogleMap.InfoWindowAdapter, VenuesRenderer.OnClusterItemRenderedListener {

    private final Context mContext;
    private Map<Marker, VenueClusterItem> mMarkerToPlaceClusterItem = new HashMap<>();


    public CustomInfoWindowAdapter(Context context){
        mContext = context;
    };

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }


    @Override
    public View getInfoContents(Marker marker) {
        if (mMarkerToPlaceClusterItem.containsKey(marker)) {
            VenueClusterItem mItem = mMarkerToPlaceClusterItem.get(marker);
            if (mItem != null) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.object_infowindow, null);

                TextView tvName = (TextView) v.findViewById(R.id.text_view_object_custom_name);
                TextView tvPrice = (TextView) v.findViewById(R.id.text_view_object_custom_price);
                tvPrice.setText("Цена: "+ Long.toString(mItem.getPrice()));

                tvName.setText(mItem.getName());

                return v;
            }
        }
        return null;
    }

    @Override
    public void onClusterItemRendered(VenueClusterItem clusterItem, Marker marker) {
        mMarkerToPlaceClusterItem.put(marker, clusterItem);
    }

    public VenueClusterItem getItem(Marker marker) {
        if (mMarkerToPlaceClusterItem.containsKey(marker)) {
            return mMarkerToPlaceClusterItem.get(marker);
        }
        return null;
    }
}
