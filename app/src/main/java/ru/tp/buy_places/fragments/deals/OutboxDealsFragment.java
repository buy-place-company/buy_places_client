package ru.tp.buy_places.fragments.deals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;

public class OutboxDealsFragment extends Fragment {

    public static OutboxDealsFragment newInstance() {
        OutboxDealsFragment fragment = new OutboxDealsFragment();
        return fragment;
    }

    public OutboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outbox_deals, container, false);
    }

}
