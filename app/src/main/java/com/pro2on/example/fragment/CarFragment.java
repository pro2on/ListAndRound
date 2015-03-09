package com.pro2on.example.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.pro2on.example.R;
import com.pro2on.example.dao.DataManager;
import com.pro2on.example.domain.Car;
import com.pro2on.example.domain.Photo;
import com.pro2on.example.util.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * Created by pro2on on 02.03.15.
 */
public class CarFragment extends Fragment {

    private static final String TAG = CarFragment.class.getSimpleName();
    public static final String EXTRA_CAR_ID = "com.pro2on.example.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private Car mCar;

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mLikedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mCarImageView;

    private String mCurrentPhotoPath;


    public static CarFragment newInstance(UUID carId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CAR_ID, carId);
        CarFragment fragment = new CarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID id = (UUID) getArguments().getSerializable(EXTRA_CAR_ID);
        mCar = DataManager.getInstance().getCarManager(getActivity()).getCar(id);

        setHasOptionsMenu(true);
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        mTitleField = (EditText) view.findViewById(R.id.car_title);
        mTitleField.setText(mCar.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCar.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLikedCheckBox = (CheckBox) view.findViewById(R.id.car_fragment_likedCheckBox);
        mLikedCheckBox.setChecked(mCar.isLiked());
        mLikedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCar.setLiked(isChecked);
            }
        });

        mDateButton = (Button) view.findViewById(R.id.car_date_button);
        //android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        //mDateButton.setText(dateFormat.format("EE, MMM dd kk:mm:ss", mCar.getDate()));
        mDateButton.setText(mCar.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        mPhotoButton = (ImageButton) view.findViewById(R.id.fragment_car_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mCarImageView = (ImageView) view.findViewById(R.id.fragment_car_imageView);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTime(mCar.getDate());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);

            calendar.set(year, month, day, hour, minute, sec);

            mCar.setDate(calendar.getTime());
            mDateButton.setText(mCar.getDate().toString());

            // And get time
            getTime();
        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            mCar.setDate(date);
            mDateButton.setText(mCar.getDate().toString());
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, mCurrentPhotoPath);
            Toast.makeText(getActivity(), mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
            Photo photo = new Photo(mCurrentPhotoPath);
            mCar.setPhoto(photo);
            showPhoto();
        }
    }

    private void getDate() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mCar.getDate());
        dialog.setTargetFragment(CarFragment.this, REQUEST_DATE);
        dialog.show(fm, DIALOG_DATE);
    }

    private void getTime() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mCar.getDate());
        dialog.setTargetFragment(CarFragment.this, REQUEST_TIME);
        dialog.show(fm, DIALOG_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        DataManager.getInstance().getCarManager(getActivity()).saveCars();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_car_menu, menu);


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "error creating file for photo", ex);
                Toast.makeText(getActivity(), "can't create file for photo", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void showPhoto() {
        Photo p = mCar.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            //String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaleDrawable(getActivity(), p.getFilename());
        }
        mCarImageView.setImageDrawable(b);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mCarImageView);
    }
}
