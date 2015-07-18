package ru.tp.buy_places.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 04.07.2015.
 */
public class VenueView {
    private final Context mContext;
    private TextView mOwnerTextView;
    private TextView mLevelTextView;
    private TextView mNameTextView;
    private FrameLayout mButtonsContainerLayout;

    public VenueView(Context context) {
        mContext = context;
    }

    public void setNameTextView(TextView nameTextView) {
        this.mNameTextView = nameTextView;
    }

    public void setOwnerTextView(TextView ownerTextView) {
        this.mOwnerTextView = ownerTextView;
    }

    public void setLevelTextView(TextView levelTextView) {
        this.mLevelTextView = levelTextView;
    }

    public void setButtonsContainerLayout(FrameLayout buttonsContainerLayout) {
        this.mButtonsContainerLayout = buttonsContainerLayout;
    }

    public TextView getNameTextView() {
        return mNameTextView;
    }

    public TextView getOwnerTextView() {
        return mOwnerTextView;
    }

    public TextView getLevelTextView() {
        return mLevelTextView;
    }

    public static TableRow createStatisticsTableRow(Context context, String name, String value) {
        final TableRow sStatisticsTableRow = (TableRow) LayoutInflater.from(context).inflate(R.layout.statistics_table_row, null);
        final TextView nameTextView = (TextView) sStatisticsTableRow.findViewById(R.id.name);
        final TextView valueTextView = (TextView) sStatisticsTableRow.findViewById(R.id.value);
        nameTextView.setText(name);
        valueTextView.setText(value);
        return sStatisticsTableRow;
    }

    public FrameLayout getButtonsContainerLayout() {
        return mButtonsContainerLayout;
    }
}
