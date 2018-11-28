package com.adc2018.bpmhw3.entity.ocr;

public class XfyunCardResult {
    String code;
    String desc;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "XfyunCardResult{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}

