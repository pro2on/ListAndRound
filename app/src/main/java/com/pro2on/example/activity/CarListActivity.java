package com.pro2on.example.activity;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.pro2on.example.fragment.CarListFragment;

/**
 * Created by pro2on on 02.03.15.
 */
public class CarListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CarListFragment();
    }
}
