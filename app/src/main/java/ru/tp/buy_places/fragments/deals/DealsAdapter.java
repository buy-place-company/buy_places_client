package ru.tp.buy_places.fragments.deals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.resourses.Deal;

/**
 * Created by Ivan on 10.06.2015.
 */
public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {
    private final Deal.DealType mType;
    OnItemClickListener mItemClickListener;
    Context mContext;
    private List<Deal> mData = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public DealsAdapter(Context context, Deal.DealType type, OnItemClickListener onItemClickListener){
        mContext = context;
        mType = type;
        mItemClickListener = onItemClickListener;
    }

    public void setData(List<Deal> data){
        mData.clear();
        if(data != null)
            mData.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mVenuesName;
        public TextView mOpponentName;
        public TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mVenuesName = (TextView) itemView.findViewById(R.id.text_view_venues_name);
            mOpponentName = (TextView) itemView.findViewById(R.id.text_view_deal_opponent);

        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }

        }
    }

    @Override
    public DealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DealsAdapter.ViewHolder holder, int position) {
        if(mData != null) {
            holder.mVenuesName.setText(mData.get(position).getVenue().getName()); // Setting the Text with the array of our Titles
            switch (mType) {
                case INCOMING:
                    holder.mOpponentName.setText(mData.get(position).getPlayerFrom().getUsername());
                    break;
                case OUTGOING:
                    holder.mOpponentName.setText(mData.get(position).getPlayerTo().getUsername());
                    break;
            }
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
}
