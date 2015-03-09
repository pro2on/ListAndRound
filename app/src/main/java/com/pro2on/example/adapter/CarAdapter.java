package com.pro2on.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro2on.example.R;
import com.pro2on.example.domain.Car;

import java.util.ArrayList;

/**
 * Created by pro2on on 04.03.15.
 */
public class CarAdapter extends ArrayAdapter<Car> {

    private static class ViewHolder {
        TextView title;
        TextView date;
        ImageView photo;
        CheckBox liked;
    }

    public CarAdapter(Context context,  ArrayList<Car> cars) {
        super(context, 0, cars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the data item for this posiion
        Car car = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.list_item_car, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.car_list_item_title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.car_list_item_date);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.car_list_item_image);
            viewHolder.liked = (CheckBox) convertView.findViewById(R.id.list_item_car_likedCheckBox);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(car.getTitle());
        viewHolder.date.setText(car.getDate().toString());
        viewHolder.liked.setChecked(car.isLiked());

        // Return the completed view to render on screen
        return convertView;
    }

}
