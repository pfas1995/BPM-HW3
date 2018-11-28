package com.adc2018.bpmhw3.network.api.ocr;

import com.adc2018.bpmhw3.entity.ocr.AliyunCardResult;
import com.adc2018.bpmhw3.entity.ocr.CardImage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 识别名片的 api
 */
public interface OCRApi {
    /**
     * 阿里云的 api
     * @param cardImage 图片的编码
     * @return
     */
    @Headers({
            "Authorization: APPCODE f42f3f44a2ac4253a4b017850edb3918",
            "Content-Type: application/json; charset=UTF-8"
    })
    @POST("/rest/160601/ocr/ocr_business_card.json")
    Call<AliyunCardResult> aliyunCard(@Body CardImage cardImage);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",

    })

    @FormUrlEncoded
    @POST("/xfyun-card-ocr")
   Call<ResponseBody> xfyunCard(@Field("image") String image);

}
