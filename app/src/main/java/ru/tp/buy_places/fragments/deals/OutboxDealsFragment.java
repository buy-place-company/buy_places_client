package ru.tp.buy_places.fragments.deals;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.DealActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Deal;
import ru.tp.buy_places.service.resourses.Deals;
import ru.tp.buy_places.utils.AccountManagerHelper;

public class OutboxDealsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,DealsAdapter.OnItemClickListener {
    private static final String AGR_TYPE = "ARG_TYPE";
    private RecyclerView mRecycleView;
    private DealsAdapter mDealsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Deal.DealType mType;

    public OutboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = (Deal.DealType) getArguments().getSerializable(AGR_TYPE);
        mDealsAdapter = new DealsAdapter(getActivity(), mType, this);
    }

    public static Fragment newInstance(Deal.DealType type) {
        Fragment fragment = new OutboxDealsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AGR_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        getLoaderManager().destroyLoader(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_objects, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.listPlacesRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mDealsAdapter);
        getLoaderManager().initLoader(0, getArguments(), this);
        return mRecycleView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Deal.DealType type = (Deal.DealType) args.getSerializable(AGR_TYPE);
        long playerId = AccountManagerHelper.getPlayerId(getActivity());
        switch (type) {
            case INCOMING:
                return new CursorLoader(getActivity(), BuyPlacesContract.Deals.CONTENT_URI, BuyPlacesContract.Deals.WITH_RELATED_ENTITIES_PROJECTION, BuyPlacesContract.Deals.WITH_SPECIFIED_PLAYER_TO_ID_OR_NULL_AND_STATUS, new String[]{Long.toString(playerId),Long.toString(playerId), Deal.DealState.UNCOMPLETED.name()}, null);
            case OUTGOING:
                return new CursorLoader(getActivity(), BuyPlacesContract.Deals.CONTENT_URI, BuyPlacesContract.Deals.WITH_RELATED_ENTITIES_PROJECTION, BuyPlacesContract.Deals.WITH_SPECIFIED_PLAYER_FROM_ID_AND_STATUS, new String[]{Long.toString(playerId), Deal.DealState.UNCOMPLETED.name()}, null);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Deals deals = Deals.fromCursor(data);
        mDealsAdapter.setData(deals.getDeals());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(View view, int position) {
        long dealRowId = mDealsAdapter.getItemId(position);
        DealActivity.start(getActivity(), dealRowId);
    }
}
