package com.pro2on.example.dao;

import android.content.Context;

/**
 * Created by pro2on on 04.03.15.
 */
public class DataManager {


    private static DataManager instance = null;
    private static CarManager carManager = null;


    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) instance = new DataManager();
            }
        }
        return instance;
    }

    public CarManager getCarManager(Context c) {
        if (carManager ==  null) {
            carManager = new CarManager(c.getApplicationContext());
        }
        return carManager;
    }
}
