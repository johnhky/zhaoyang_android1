package com.doctor.sun.entity;

/**
 * Created by heky on 17/5/12.
 */

public class ClinicAddress {
    private String address;
    private String clinic;
    private boolean display;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

}
