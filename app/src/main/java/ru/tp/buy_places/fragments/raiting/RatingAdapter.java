package ru.tp.buy_places.fragments.raiting;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.UserActivity;
import ru.tp.buy_places.service.resourses.Player;

/**
 * Created by home on 21.05.2015.
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private LayoutInflater inflater;
    Activity activity;
    private List<Player> mData = new ArrayList<>();

    RatingAdapter(Context context){
        inflater = LayoutInflater.from(context);
        activity = (Activity)context;
    }

    public void setData(List<Player> data){
        mData.clear();
        if(data != null)
          mData.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mScore;
        public ImageView mIcon;
        public TextView mRating;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.text_view_name);
            mScore = (TextView) itemView.findViewById(R.id.tv_price);
            mRating = (TextView) itemView.findViewById(R.id.tv_rating_number);
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_rating);

        }
    }

    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raiting, parent, false);
        return new ViewHolder(v);
     }

    @Override
    public void onBindViewHolder(RatingAdapter.ViewHolder holder, final int position) {
        if(mData != null) {

            holder.mTitle.setText(mData.get(position).getUsername());
            holder.mScore.setText(Long.toString(mData.get(position).getCash()));
            //Picasso.with(activity).load(mData.get(position).getAvatar()).error(R.drawable.ic_launcher).into(holder.mIcon);
            holder.mRating.setText(Long.toString(mData.get(position).getPosition()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserActivity.start(activity, mData.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
