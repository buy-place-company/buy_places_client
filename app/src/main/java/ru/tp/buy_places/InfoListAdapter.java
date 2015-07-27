package ru.tp.buy_places;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.activities.UserActivity;
import ru.tp.buy_places.activities.VenueActivity;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Player;

/**
 * Created by Ivan on 27.07.2015.
 */
public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.ViewHolder> {

    private final Context mContext;
    List<InfoItem> mInfoItems = new ArrayList<>();

    public InfoListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public InfoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_venues_info, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InfoListAdapter.ViewHolder holder, final int position) {
        holder.venueInfoKey.setText(mInfoItems.get(position).getKey());
        holder.venueInfoValue.setText(mInfoItems.get(position).getValue());
        switch (mInfoItems.get(position).mType) {
            case PRICE:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cash_usd_grey600_18dp, 0, 0, 0);
                break;
            case CHECKINS:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_foursquare_grey600_18dp, 0, 0, 0);
                break;
            case PLAYER:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_account_grey600_18dp, 0, 0, 0);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserActivity.start(mContext, (Player) mInfoItems.get(position).getOnClickModel());
                    }
                });
                break;
            case LEVEL:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_crown_grey600_18dp, 0, 0, 0);
                break;
            case VENUE:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_grey600_18dp, 0, 0, 0);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Place venue = (Place) mInfoItems.get(position).getOnClickModel();
                        VenueActivity.start((Activity) mContext, venue.getRowId(), new LatLng(venue.getLatitude(), venue.getLongitude()), VenueActivity.VenueType.fromVenue(venue));
                    }
                });
                break;
            case DATE:
                //holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_grey600_18dp, 0,0,0);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mInfoItems.size();
    }

    public void setInfoItems(List<InfoItem> infoItems) {
        mInfoItems.clear();
        if (infoItems != null)
            mInfoItems.addAll(infoItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView venueInfoKey;
        public TextView venueInfoValue;

        public ViewHolder(View itemView) {
            super(itemView);
            venueInfoKey = (TextView) itemView.findViewById(R.id.text_view_venues_info_key);
            venueInfoValue = (TextView) itemView.findViewById(R.id.text_view_venues_info_value);
        }
    }

    public static class InfoItem {
        private String mKey;
        public InfoType mType;
        private String mValue;
        private Object mOnClickModel;

        public InfoItem(String key, InfoType type, String value, Object onClickModel) {
            mKey = key;
            mType = type;
            mValue = value;
            mOnClickModel = onClickModel;
        }

        public InfoItem(String key, InfoType type, String value) {
            this(key, type, value, null);
        }

        public String getKey() {
            return mKey;
        }

        public String getValue() {
            return mValue;
        }

        public Object getOnClickModel() {
            return mOnClickModel;
        }
    }

    public enum InfoType {
        PRICE,
        CHECKINS,
        PLAYER,
        LEVEL,
        VENUE,
        DATE
    }
}
