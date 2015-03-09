package com.pro2on.example.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by pro2on on 07.03.15.
 */
public class PictureUtils {

    public static BitmapDrawable getScaleDrawable(Activity activity, String path) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int destWidth = size.x;
        int destHeight = size.y;

        //Чтение размеров на диске
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int scaleFactor = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            scaleFactor = Math.round(srcHeight / destHeight);
        } else {
            scaleFactor = Math.round(srcWidth / destWidth);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }

    public static void cleanImageView(ImageView imageView) {
        if (!(imageView.getDrawable() instanceof BitmapDrawable)) return;

        BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }

}
