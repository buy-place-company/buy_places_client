package ru.tp.buy_places.fragments.deals;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import ru.tp.buy_places.R;
import ru.tp.buy_places.service.ServiceHelper;

public class DealsFragment extends Fragment {


    private DealsPagerAdapter mDealsPagerAdapter;
    private ParallaxViewPager mViewPager;
    private TabLayout mTabLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;

    public static DealsFragment newInstance() {
        DealsFragment fragment = new DealsFragment();
        return fragment;
    }

    public DealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDealsPagerAdapter = new DealsPagerAdapter(getChildFragmentManager());
        ServiceHelper.get(getActivity()).getDeals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deals, container, false);
        mTabLayout = (TabLayout) inflater.inflate(R.layout.tab_layout, null);
        mViewPager = (ParallaxViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.setScaleType(ParallaxViewPager.FIT_WIDTH);
        //mViewPager.setBackgroundResource(R.drawable.mainback_1);
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
        mViewPager.setAdapter(mDealsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mAppBarLayout.addView(mTabLayout);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAppBarLayout.removeView(mTabLayout);
    }
}
