package ru.tp.buy_places.fragments.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.R;
import ru.tp.buy_places.map.ObjectRenderer;
import ru.tp.buy_places.map.PlaceClusterItem;

/**
 * Created by home on 11.05.2015.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, ObjectRenderer.OnClusterItemRenderedListener {

    private final Context mContext;
    private Map<Marker, PlaceClusterItem> mMarkerToPlaceClusterItem = new HashMap<>();

    CustomInfoWindowAdapter(Context context){
        mContext = context;
    };

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }


    @Override
    public View getInfoContents(Marker marker) {
        if (mMarkerToPlaceClusterItem.containsKey(marker)) {
            PlaceClusterItem item = mMarkerToPlaceClusterItem.get(marker);
            if (item != null) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.object_infowindow, null);
                Button buyButton = (Button) v.findViewById(R.id.button_buy_object);
                TextView tvName = (TextView) v.findViewById(R.id.text_view_object_custom_name);
                TextView tvPrice = (TextView) v.findViewById(R.id.text_view_object_custom_price);
                tvPrice.setText(Long.toString(item.getPrice()));
                tvName.setText(item.getName());
                return v;
            }
        }
        return null;
    }

    @Override
    public void onClusterItemRendered(PlaceClusterItem clusterItem, Marker marker) {
        mMarkerToPlaceClusterItem.put(marker, clusterItem);
    }
}
