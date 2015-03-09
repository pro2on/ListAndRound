package com.pro2on.example.dao;

import android.content.Context;

import com.pro2on.example.domain.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by pro2on on 07.03.15.
 */
public class CarIntentJSONSerializer {
    private Context mContext;
    private String mFileName;

    public CarIntentJSONSerializer(Context context, String fileName) {
        this.mContext = context;
        this.mFileName = fileName;
    }

    public void saveCars(ArrayList<Car> cars) throws IOException, JSONException {

        //Строим массив JSON
        JSONArray array = new JSONArray();
        for (Car c: cars) {
            array.put(c.toJSON());
        }

        // Запишем файл на диск
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    public ArrayList<Car> loadCars() throws JSONException, IOException {
        ArrayList<Car> cars = new ArrayList<Car>();
        BufferedReader reader = null;
        try {
            // Открытие и чтение файла в StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // Разбор JSON
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i ++) {
                cars.add(new Car(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //  Саоме первое обращение... не обращать внимания
        } finally {
            if (reader != null) reader.close();
        }
        return cars;
    }
}
