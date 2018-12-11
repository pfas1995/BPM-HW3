package com.adc2018.bpmhw3.entity.amap;

public class Position {
    private String province;
    private String city;
    private String district;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        String ret = "";
        if(province != null) ret += province;
        if(city != null) ret += city;
        if(district != null) ret += district;
        return ret;
    }

    @Override
    public String toString() {
        return "Position{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
