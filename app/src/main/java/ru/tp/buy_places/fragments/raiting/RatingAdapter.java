package ru.tp.buy_places.fragments.raiting;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Player;

/**
 * Created by home on 21.05.2015.
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private LayoutInflater inflater;
    OnItemClickListener mItemClickListener;
    Activity activity;
    private List<Player> mData = new ArrayList<>();
    private static int count = 0;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mScore;
        public ImageView mIcon;
        public TextView mRating;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.text_view_rating);
            mScore = (TextView) itemView.findViewById(R.id.tv_price);
            mRating = (TextView) itemView.findViewById(R.id.tv_rating_number);
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_rating);

        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(view, getPosition());
            }

        }
    }

    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raiting, parent, false);
        return new ViewHolder(v);
     }

    @Override
    public void onBindViewHolder(RatingAdapter.ViewHolder holder, int position) {
        if(mData != null) {
            count++;
            holder.mTitle.setText(mData.get(position).getUsername());
            holder.mScore.setText(Long.toString(mData.get(position).getScore()));
            //Picasso.with(activity).load(mData.get(position).getAvatar()).error(R.drawable.ic_launcher).into(holder.mIcon);
            holder.mRating.setText(Integer.toString(count));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
