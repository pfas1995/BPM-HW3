package com.adc2018.bpmhw3.network;

import android.util.Log;

import com.adc2018.bpmhw3.entity.AliyunCardResult;
import com.adc2018.bpmhw3.entity.CardImage;
import com.adc2018.bpmhw3.entity.Test;
import com.adc2018.bpmhw3.network.api.ocr.OCRApi;
import com.adc2018.bpmhw3.network.api.ocr.OCRUtil;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.network.api.rmp.TestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HttpRequest {

    /**
     * api 调用
     * onResponsec 调用成功执行
     * onFailure 调用失败执行
     * @param call 调用的api
     * @param <T> response body 的类型
     */
    private <T> void  callEnqueue(Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.d(HttpRequest.class.getSimpleName(), response.body().toString());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(HttpRequest.class.getSimpleName(), t.getMessage());
            }
        });
    }


    /**
     * 测试
     */
    public void getAllTest() {
        Retrofit retrofit = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl());
        final TestApi api = retrofit.create(TestApi.class);
        Call<Object> call = api.getAllTest();
        callEnqueue(call);
     }

    /**
     * 测试依据id获取
     * @param id
     */
     public void getTestById(String id) {
         Retrofit retrofit = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl());

         final TestApi api = retrofit.create(TestApi.class);
         Call<Test> call = api.getTestById("1542954361589");
         callEnqueue(call);
     }

    /**
     * 测试获取所有
     * @param test
     */
     public void addTestItem(Test test) {
         Retrofit retrofit = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl());
         final TestApi api = retrofit.create(TestApi.class);
         Call<Test> call = api.addTestItem(test);
         callEnqueue(call);
     }

    /**
     * 阿里云的名片识别请求
     * @param imageEncode 名片图片的 base64 编码
     */
    public void aliyunCardOCR(String imageEncode) {
        Retrofit retrofit = RetrofitTool.getRetrofit(OCRUtil.getAliyunOCRBaseUrl());
        final OCRApi api = retrofit.create(OCRApi.class);
        CardImage cardImage = new CardImage();
        cardImage.setImage(imageEncode);
        Call<AliyunCardResult> call = api.aliyunCard(cardImage);
        callEnqueue(call);
     }

//    /**
//     * 科大讯飞云的名片请求识别
//     * @param imageEncode 名片图片的 base64 编码
//     */
//    public void xfyunCardOCR(String imageEncode) {
//        Retrofit retrofit = RetrofitTool.getRetrofit(OCRUtil.getXfyunOCRBaseUrl());
//        final OCRApi api = retrofit.create(OCRApi.class);
//        CardImage cardImage = new CardImage();
//        Log.d(HttpRequest.class.getSimpleName(), "xfyunCardOCR: " + imageEncode);
//        try {
//            imageEncode = URLEncoder.encode(imageEncode, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            Log.e(HttpRequest.class.getSimpleName(), "xfyunCardOCR: " + e.getMessage() );
//        }
//        cardImage.setImage(imageEncode);
//        Log.d(HttpRequest.class.getSimpleName(), "xfyunCardOCR: " + imageEncode);
//        String apiKey = "43f05d7fde83a6170efd314e9a376d76";
//        String xParamBase64 = "eyJlbmdpbmVfdHlwZSI6ICJidXNpbmVzc19jYXJkIn0=";
//        String xCurTime = System.currentTimeMillis() / 1000L + "";
//        String xCheckSum = new String(Hex.encodeHex(DigestUtils.md5(apiKey + xCurTime + xParamBase64)));
//        Call<XfyunCardResult> call = api.xfyunCard(xCurTime, xCheckSum, cardImage);
//        callEnqueue(call);
//     }
}
