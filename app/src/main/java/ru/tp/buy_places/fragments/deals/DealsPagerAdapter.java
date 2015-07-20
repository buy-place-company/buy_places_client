package ru.tp.buy_places.fragments.deals;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import ru.tp.buy_places.service.resourses.Deal;

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
                return OutboxDealsFragment.newInstance(Deal.DealType.INCOMING);
            case 1:
                return OutboxDealsFragment.newInstance(Deal.DealType.OUTGOING);
            default:
                throw new IllegalStateException();
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
                throw new IllegalStateException();
        }
    }
}
