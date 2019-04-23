package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VendorList implements Serializable {

    private List<Vendor> vendors;

    public VendorList() {
        vendors = new ArrayList<>();
    }

    public VendorList(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }
}
