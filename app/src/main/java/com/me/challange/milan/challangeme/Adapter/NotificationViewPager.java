package com.me.challange.milan.challangeme.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.me.challange.milan.challangeme.Fragment.NotificationGetChallangeFragment;
import com.me.challange.milan.challangeme.Fragment.NotificationSendChallangeFragment;

/**
 * Created by milan on 10/29/2017.
 */
public class NotificationViewPager extends FragmentPagerAdapter {
    public NotificationViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                NotificationGetChallangeFragment notificationGetChallangeFragment=new NotificationGetChallangeFragment();
                return notificationGetChallangeFragment;
            case 1:
                NotificationSendChallangeFragment notificationSendChallangeFragment=new NotificationSendChallangeFragment();
                return notificationSendChallangeFragment;
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
                return "Received Challange";
            case 1:
                return "Send Challanges";
        }
        return null;
    }
}
