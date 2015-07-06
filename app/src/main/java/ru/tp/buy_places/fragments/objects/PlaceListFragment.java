package ru.tp.buy_places.fragments.objects;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;

public class PlaceListFragment extends Fragment {


    private PlacePagerAdapter mPlacePagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;


    public PlaceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlacePagerAdapter = new PlacePagerAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placelist, container, false);
        mTabLayout = (TabLayout) inflater.inflate(R.layout.tab_layout, null);
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewPager.setAdapter(mPlacePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mAppBarLayout.addView(mTabLayout);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAppBarLayout.removeView(mTabLayout);
    }
}
