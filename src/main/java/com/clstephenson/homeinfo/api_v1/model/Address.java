package com.clstephenson.homeinfo.api_v1.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class Address {

    @Size(max = 100)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String stateOrProvince;

    @Size(max = 100)
    private String country = "United States";

    @Size(max = 10)
    private String postalCode;

    public Address() {
    }

    public Address(String address, String city, String stateOrProvince, String postalCode) {
        this.address = address;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
