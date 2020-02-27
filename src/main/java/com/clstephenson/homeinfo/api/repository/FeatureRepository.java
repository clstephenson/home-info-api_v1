package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.Feature;
import org.springframework.data.repository.CrudRepository;

public interface FeatureRepository extends CrudRepository<Feature, Long> {

    Iterable<Feature> findAllByPropertyId(long propertyId);

    Iterable<Feature> findAllByPropertyIdAndLocationId(long propertyId, long locationId);

    Iterable<Feature> findAllByLocationId(long locationId);

}
