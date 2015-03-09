package com.pro2on.example.fragment;

import android.app.Fragment;
import android.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


import com.melnykov.fab.FloatingActionButton;
import com.pro2on.example.R;

import com.pro2on.example.activity.CarPagerActivity;
import com.pro2on.example.adapter.CarAdapter;
import com.pro2on.example.dao.DataManager;
import com.pro2on.example.domain.Car;

import java.util.ArrayList;


/**
 * Created by pro2on on 02.03.15.
 */
public class CarListFragment extends Fragment {

    private static final String TAG = CarListFragment.class.getSimpleName();

    private ArrayList<Car> mCars;
    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_activity_list);
        mCars = DataManager.getInstance().getCarManager(getActivity()).getCars();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_list, container, false);

        mListView = (ListView) v.findViewById(R.id.fragment_car_list_carListView);
        CarAdapter adapter = new CarAdapter(getActivity(), mCars);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car car = (Car) mListView.getAdapter().getItem(position);

                // Запустим детализацию машинки
                Intent intent = new Intent(getActivity(), CarPagerActivity.class);
                intent.putExtra(CarFragment.EXTRA_CAR_ID, car.getId());
                startActivity(intent);
            }
        });

        //View emptyView = inflater.inflate(R.layout.list_item_car, container, false);
        mListView.setEmptyView(v.findViewById(R.id.empty));
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.car_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_car:
                        CarAdapter adapter = (CarAdapter)mListView.getAdapter();
                        for (int i = adapter.getCount() - 1; i >= 0; i --) {
                            if (mListView.isItemChecked(i)) {
                                DataManager.getInstance().getCarManager(getActivity()).deleteCar(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;

                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fragmetn_car_list_fab);
        fab.attachToListView(mListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = new Car();
                DataManager.getInstance().getCarManager(getActivity()).addCar(car);

                // Запустим детализацию машинки
                Intent intent = new Intent(getActivity(), CarPagerActivity.class);
                intent.putExtra(CarFragment.EXTRA_CAR_ID, car.getId());
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CarAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }
}
