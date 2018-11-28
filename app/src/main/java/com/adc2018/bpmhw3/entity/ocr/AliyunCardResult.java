package com.adc2018.bpmhw3.entity.ocr;

import java.util.List;

public class AliyunCardResult {
    String name;
    List<String> company;
    List<String> department;
    List<String> title;
    List<String> tel_cell;
    List<String> tel_work;
    List<String> addr;
    List<String> email;
    String request_id;
    Boolean success;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCompany() {
        return company;
    }

    public void setCompany(List<String> company) {
        this.company = company;
    }

    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getTel_cell() {
        return tel_cell;
    }

    public void setTel_cell(List<String> tel_cell) {
        this.tel_cell = tel_cell;
    }

    public List<String> getTel_work() {
        return tel_work;
    }

    public void setTel_work(List<String> tel_work) {
        this.tel_work = tel_work;
    }

    public List<String> getAddr() {
        return addr;
    }

    public void setAddr(List<String> addr) {
        this.addr = addr;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "AliyunCardResult{" +
                "name='" + name + '\'' +
                ", company=" + company +
                ", department=" + department +
                ", title=" + title +
                ", tel_cell=" + tel_cell +
                ", tel_work=" + tel_work +
                ", addr=" + addr +
                ", email=" + email +
                ", request_id='" + request_id + '\'' +
                ", success=" + success +
                '}';
    }
}
