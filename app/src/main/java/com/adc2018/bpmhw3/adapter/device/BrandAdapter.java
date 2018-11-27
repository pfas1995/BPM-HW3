package com.adc2018.bpmhw3.adapter.device;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;

public class BrandAdapter {
    /**
     * 适配不同品牌手机拍照后图片发生翻转的情形
     * @param bitmap 图片
     * @return
     */
    public static Bitmap rotatePhotoAfterCamera(Bitmap bitmap) {
        switch (Build.BRAND){
            case "Xiaomi":
            case "google":
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(),
                        matrix, true);
                break;
                default:
        }
        return bitmap;
    }
}
