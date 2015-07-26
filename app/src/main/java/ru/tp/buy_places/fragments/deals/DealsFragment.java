package ru.tp.buy_places.fragments.deals;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.ServiceHelper;

public class DealsFragment extends Fragment implements ViewPager.OnPageChangeListener {


    public static final String KEY_DEALS_PAGE = "KEY_DEALS_PAGE";
    private DealsPagerAdapter mDealsPagerAdapter;
    private ViewPager mViewPager;
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
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(this);
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
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int position = preferences.getInt(KEY_DEALS_PAGE, 0);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAppBarLayout.removeView(mTabLayout);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_DEALS_PAGE, position).apply();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
