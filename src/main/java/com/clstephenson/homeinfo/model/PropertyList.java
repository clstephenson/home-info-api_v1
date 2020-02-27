package com.clstephenson.homeinfo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyList implements Serializable {

    private List<Property> properties;

    public PropertyList() {
        properties = new ArrayList<>();
    }

    public PropertyList(List<Property> properties) {
        this.properties = properties;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
