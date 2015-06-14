package ru.tp.buy_places.fragments.deals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;

public class OutboxDealsFragment extends Fragment {
    private static final String TEXT_FIELD = "text";
    private static final String IMAGE_FIELD = "image";
    private RecyclerView mRecycleView;
    private DealsAdapter mDealsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public OutboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDealsAdapter = new DealsAdapter(getActivity());
    }

    public static Fragment newInstance() {
        Fragment fragment = new OutboxDealsFragment();
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
        return mRecycleView;
    }

}
