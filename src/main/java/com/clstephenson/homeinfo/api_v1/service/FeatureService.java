package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Feature;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FeatureService {
    List<Feature> getAll();

    List<Feature> findByPropertyId(long propertyId);

    List<Feature> findByPropertyIdAndLocationId(long propertyId, long locationId);

    Optional<Feature> findById(long id);

    Feature save(Feature feature);

    void deleteById(long id);

    boolean existsById(long id);
}
