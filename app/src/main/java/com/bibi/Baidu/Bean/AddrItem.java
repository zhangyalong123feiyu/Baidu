package com.bibi.Baidu.Bean;

import java.io.Serializable;

/**
 * Created by bibinet on 2016/11/1.
 */
public class AddrItem implements Serializable {

    public double latitude, longitude;
    public String name, address;
    public String city, cityCode;

    public AddrItem() {

    }

    public AddrItem(double latitude, double longitude, String name, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
    }

    public AddrItem(double latitude, double longitude, String name, String address, String city, String cityCode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.city = city;
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "AddrItem [latitude=" + latitude + ", longitude=" + longitude + ", name=" + name + ", address="
                + address + ", city=" + city + ", cityCode=" + cityCode + "]";
    }

}
