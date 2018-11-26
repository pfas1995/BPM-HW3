package com.adc2018.bpmhw3.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BPM3 {
    public static Bitmap changeBitmapScale(Bitmap bitmap, int nwidth, int nheight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float)nwidth) / width;
        float scaleHeight = ((float)nheight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bm;
    }
}
