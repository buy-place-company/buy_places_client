package ru.tp.buy_places.fragments.deals;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Ivan on 07.04.2015.
 */
public class DealsPagerAdapter extends FragmentPagerAdapter {

    public DealsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return OutboxDealsFragment.newInstance();
            case 1:
                return OutboxDealsFragment.newInstance();
            default:
                return OutboxDealsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Входящие";
            case 1:
                return "Исходящие";
            default:
                return "Входящие";
        }
    }
}
