package com.me.challange.milan.challangeme.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.me.challange.milan.challangeme.Fragment.HomeChallangersFragment;
import com.me.challange.milan.challangeme.Fragment.HomeNewsFragment;

/**
 * Created by milan on 10/28/2017.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeNewsFragment homeNewsFragment = new HomeNewsFragment();
                return homeNewsFragment;
            case 1:
                HomeChallangersFragment homeChallangersFragment = new HomeChallangersFragment();
                return homeChallangersFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Open Challanges";
            case 1:
                return "Challangers";
        }
        return null;
    }
}
