package com.me.challange.milan.challangeme;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.challange.milan.challangeme.Adapter.NotificationViewPager;

public class NotificationActivity extends AppCompatActivity {
    public static NotificationActivity instance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;

        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface type = Typeface.createFromAsset(getAssets(), "font/regular.ttf");
        TextView toolbarTitleName=(TextView)findViewById(R.id.notificationToolbarTitleName);
        toolbarTitleName.setTypeface(type);

        ViewPager viewPager=(ViewPager)findViewById(R.id.notification_viewPager);
        NotificationViewPager notificationViewPager=new NotificationViewPager(getSupportFragmentManager());
        viewPager.setAdapter(notificationViewPager);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.notification_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        changeTabsFont(tabLayout, type);

        ImageView notificationBackIcon=(ImageView)findViewById(R.id.notification_back_icon);
        notificationBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slid_in_left,R.anim.slide_out_right);
            }
        });

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

    public static NotificationActivity getInstance(){
        return instance;
    }
}
