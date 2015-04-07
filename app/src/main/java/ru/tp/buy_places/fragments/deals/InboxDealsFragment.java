package ru.tp.buy_places.fragments.deals;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;

public class InboxDealsFragment extends Fragment {

    public static InboxDealsFragment newInstance() {
        InboxDealsFragment fragment = new InboxDealsFragment();
        return fragment;
    }

    public InboxDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_deals, container, false);
    }
}
