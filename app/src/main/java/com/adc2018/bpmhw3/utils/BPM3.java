package com.adc2018.bpmhw3.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.adc2018.bpmhw3.entity.amap.Position;
import com.adc2018.bpmhw3.entity.rmp.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class BPM3 {
    private static String TAG = BPM3.class.getSimpleName();

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

    //登录用户
    public static User user;

    public static Map<String, Object> activity = new HashMap<>();


    private static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String timeFormat(long ms) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        return format.format(new Date(ms));
    }

    public static  Position parserAmap(ResponseBody responseBody) {
        try {
            String response = responseBody.string();
            Log.d(TAG, "parserAmap: " + response);
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String info = jsonObject.getString("info");
            Log.d(TAG, "parserAmap: "  + status + " " + info);
            if(status.equals("1")) {
                JSONObject addressComponent = jsonObject.getJSONObject("regeocode").getJSONObject("addressComponent");
                Position position = new Position();
                position.setProvince(addressComponent.getString("province"));
                String city = addressComponent.getString("city");
                if(!city.equals("[]")) {
                    position.setCity(city);
                }
                position.setDistrict(addressComponent.getString("district"));
                return position;
            }
            else {
                return null;
            }
        } catch (JSONException e) {
            Log.e(TAG, "parserAmap: ", e);
        } catch (IOException e) {
            Log.e(TAG, "parserAmap: ", e);
        }
        return null;
    }
}
