package ru.tp.buy_places.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
            final VenueClusterItem clusterItem = getItem(marker);
            if (clusterItem != null) {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.venues_infowindow, null);
                TextView nameTextView = (TextView) view.findViewById(R.id.text_view_venues_name);
                ImageView iconImageView = (ImageView) view.findViewById(R.id.image_view_venues_icon);
                TextView priceTextView = (TextView) view.findViewById(R.id.text_view_venues_price);
                TextView levelTextView = (TextView) view.findViewById(R.id.text_view_venues_level);
                TextView checkinsCountTextView = (TextView) view.findViewById(R.id.text_view_venues_checkins_count);
                if (clusterItem.getName() != null)
                    nameTextView.setText(clusterItem.getName());
                iconImageView.setImageResource(R.mipmap.ic_object);
                if (clusterItem.getBuyPrice() >= 0)
                    priceTextView.setText(Long.toString(clusterItem.getBuyPrice()));
                if (clusterItem.getLevel() >= 0)
                    levelTextView.setText(Integer.toString(clusterItem.getLevel()));
                if (clusterItem.getCheckinsCount() >= 0)
                    checkinsCountTextView.setText(Long.toString(clusterItem.getCheckinsCount()));
                return view;
            } else {
                return null;
            }
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

    public void clearItems() {
        mMarkerToPlaceClusterItem.clear();
    }
}
