package com.pro2on.example.dao;

import android.content.Context;
import android.util.Log;

import com.pro2on.example.domain.Car;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by pro2on on 04.03.15.
 */
public class CarManager {

    private static final String TAG = CarManager.class.getSimpleName();
    private static final String FILENAME = "cars.json";

    private ArrayList<Car> mCars;
    private Context mAppContext;
    private CarIntentJSONSerializer mSerializer;


    public CarManager(Context appContext) {
        this.mAppContext = appContext;
        mSerializer = new CarIntentJSONSerializer(mAppContext, FILENAME);

        try {
            mCars = mSerializer.loadCars();
            Log.d(TAG, "cars loaded from file");
        } catch (Exception e) {
            mCars = new ArrayList<Car>();
            Log.e(TAG, "Error loading cars: ", e);
        }
        Log.d(TAG, "object created");
    }

    public ArrayList<Car> getCars() {
        return mCars;
    }

    private void fillArrayDummyData() {
        for (int i = 1; i <= 100; i ++) {
            Car car = new Car();
            car.setTitle("Car #" + i);
            if (i % 2 == 0) car.setLiked(true);
            mCars.add(car);
        }
    }

    public Car getCar(UUID id) {
        for (Car car : mCars) {
            if (car.getId().equals(id)) return car;
        }
        return null;
    }

    public void addCar(Car car) {
        mCars.add(car);
    }

    public void deleteCar(Car car) {
        mCars.remove(car);
    }

    public boolean saveCars() {
        try {
            mSerializer.saveCars(mCars);
            Log.d(TAG, "cars saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving cars: ", e);
            return false;
        }
    }

}
