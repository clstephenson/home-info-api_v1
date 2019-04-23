package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeatureList implements Serializable {

    private List<Feature> features;

    public FeatureList() {
        features = new ArrayList<>();
    }

    public FeatureList(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
