package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.model.Feature;
import org.springframework.data.repository.CrudRepository;

public interface FeatureRepository extends CrudRepository<Feature, Long> {

    Iterable<Feature> findAllByPropertyId(long propertyId);

    Iterable<Feature> findAllByPropertyIdAndLocationId(long propertyId, long locationId);

}
