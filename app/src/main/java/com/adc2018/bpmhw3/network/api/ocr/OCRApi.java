package com.adc2018.bpmhw3.network.api.ocr;

import com.adc2018.bpmhw3.entity.AliyunCardResult;
import com.adc2018.bpmhw3.entity.CardImage;
import com.adc2018.bpmhw3.entity.XfyunCardResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
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
            "Content-Type: application/x-www-form-urlencoded; charset=utf-8",
            "X-Param: eyJlbmdpbmVfdHlwZSI6ICJidXNpbmVzc19jYXJkIn0=",
            "X-Appid: 5bf7a5c9"
    })
    @POST("/v1/service/v1/ocr/business_card")
    Call<XfyunCardResult> xfyunCard(@Header("X-CurTime") String xCurTime,
                                    @Header("X-CheckSum") String xCheckSum,
                                    @Body CardImage cardImage);
}