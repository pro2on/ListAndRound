package com.pro2on.example.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.pro2on.example.R;
import com.pro2on.example.dao.DataManager;
import com.pro2on.example.domain.Car;
import com.pro2on.example.fragment.CarFragment;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by pro2on on 05.03.15.
 */
public class CarPagerActivity extends ActionBarActivity {

    private static final String TAG = CarPagerActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private ArrayList<Car> mCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.carViewPager);
        setContentView(mViewPager);

        mCars = DataManager.getInstance().getCarManager(this).getCars();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Car car = mCars.get(i);
                return CarFragment.newInstance(car.getId());
            }

            @Override
            public int getCount() {
                return mCars.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Car car = mCars.get(i);
                if (car.getTitle() != null) {
                    setTitle(car.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        UUID id = (UUID) getIntent().getSerializableExtra(CarFragment.EXTRA_CAR_ID);
        for (int i = 0; i < mCars.size(); i++) {
            if (mCars.get(i).getId().equals(id)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
