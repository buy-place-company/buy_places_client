package ru.tp.buy_places.fragments.objects;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.VenueActivity;
import ru.tp.buy_places.service.resourses.Place;

/**
 * Created by home on 21.05.2015.
 */
public class MyPlacesAdapter extends RecyclerView.Adapter<MyPlacesAdapter.ViewHolder> {
    Activity activity;
    private List<Place> mData = new ArrayList<>();


    MyPlacesAdapter(Context context){
        activity = (Activity)context;
    }

    public void setData(List<Place> data){
        mData.clear();
        if(data != null)
          mData.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mLevel;
        public ImageView mIcon;
        public TextView mCategory;
        public TextView mCheckins;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.text_view_name);
            mLevel = (TextView) itemView.findViewById(R.id.tv_level);
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_rating);
            mCategory = (TextView) itemView.findViewById(R.id.tv_category);
            mCheckins = (TextView) itemView.findViewById(R.id.tv_chekins);
        }

    }

    @Override
    public MyPlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object_my, parent, false);
        return new ViewHolder(v);
     }

    @Override
    public void onBindViewHolder(MyPlacesAdapter.ViewHolder holder, final int position) {
        if(mData != null) {
            holder.mTitle.setText(mData.get(position).getName()); // Setting the Text with the array of our Titles
            holder.mIcon.setImageResource(R.mipmap.ic_object);
            holder.mLevel.setText(Integer.toString(mData.get(position).getLevel()));
            holder.mCategory.setText(mData.get(position).getCategory());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final long venuesRowId = mData.get(position).getRowId();
                    final LatLng venuesLocation = new LatLng(mData.get(position).getLatitude(), mData.get(position).getLongitude());
                    final VenueActivity.VenueType venuesType = VenueActivity.VenueType.fromVenue(mData.get(position));
                    VenueActivity.start(activity, venuesRowId, venuesLocation, venuesType);
                }
            });
            holder.mCheckins.setText(Long.toString(mData.get(position).getCheckinsCount()));

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getRowId();
    }

    public Place getItem(int position) {
        return mData.get(position);
    }
}
