package com.me.challange.milan.challangeme.Fragment;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.challange.milan.challangeme.Adapter.HomeViewPagerAdapter;
import com.me.challange.milan.challangeme.R;

/**
 * Created by milan on 10/28/2017.
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView titleName=(TextView)getActivity().findViewById(R.id.toolbarTitleName);
        titleName.setText("Challange Me");

        ViewPager viewPager=(ViewPager)getActivity().findViewById(R.id.home_viewPager);
        HomeViewPagerAdapter homeViewPagerAdapter=new HomeViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(homeViewPagerAdapter);
        TabLayout tabLayout=(TabLayout)getActivity().findViewById(R.id.home_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "font/regular.ttf");
        changeTabsFont(tabLayout,type);

    }

    private void changeTabsFont(TabLayout tabLayout,Typeface type) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(type);
                }
            }
        }
    }
}
