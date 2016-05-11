package com.sked.razaform;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class ImageActivity extends Activity {
    CustomPagerAdapter customPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        customPagerAdapter=new CustomPagerAdapter(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(customPagerAdapter);
    }
}
