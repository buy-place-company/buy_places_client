package ru.tp.buy_places;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.activities.PlaceActivity;

/**
 * Created by Ivan on 21.07.2015.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {


    private final Context mContext;
    private List<PlaceActivity.InfoItem> mInfoItems = new ArrayList<>();

    public void setData(List<PlaceActivity.InfoItem> infoItems) {
        if (infoItems == null)
            mInfoItems.clear();
        else
            mInfoItems.addAll(infoItems);
        notifyDataSetChanged();
    }

    public void add(PlaceActivity.InfoItem s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mInfoItems.add(position, s);
        notifyItemInserted(position);
    }


    public void remove(int position){
        if (position < getItemCount()  ) {
            mInfoItems.remove(position);
            notifyItemRemoved(position);
        }
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView venueInfoKey;
        public TextView venueInfoValue;


        public SimpleViewHolder(View view) {
            super(view);
            venueInfoKey = (TextView) itemView.findViewById(R.id.text_view_venues_info_key);
            venueInfoValue = (TextView) itemView.findViewById(R.id.text_view_venues_info_value);
        }
    }


    public SimpleAdapter(Context context) {
        mContext = context;
    }


    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_venues_price_info, parent, false);
        return new SimpleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.venueInfoKey.setText(mInfoItems.get(position).getKey());
        holder.venueInfoValue.setText(mInfoItems.get(position).getValue());
        switch (mInfoItems.get(position).mType) {
            case PRICE:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cash_usd_grey600_18dp, 0, 0, 0);
                break;
            case CHECKINS:
                holder.venueInfoValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_foursquare_grey600_18dp, 0, 0, 0);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mInfoItems.size();
    }
}
