package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Feature;
import com.clstephenson.homeinfo.api_v1.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    FeatureRepository featureRepository;

    @Override
    public List<Feature> getAll() {
        return StreamSupport.stream((featureRepository.findAll().spliterator()), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Feature> findByPropertyId(long propertyId) {
        return StreamSupport.stream(featureRepository.findAllByPropertyId(propertyId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Feature> findByPropertyIdAndLocationId(long propertyId, long locationId) {
        return StreamSupport.stream(featureRepository.findAllByPropertyIdAndLocationId(propertyId, locationId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Feature> findByLocationId(long locationId) {
        return StreamSupport.stream(featureRepository.findAllByLocationId(locationId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Feature> findById(long id) {
        return featureRepository.findById(id);
    }

    @Override
    public Feature save(Feature feature) {
        return featureRepository.save(feature);
    }

    @Override
    public void deleteById(long id) {
        featureRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return featureRepository.existsById(id);
    }
}
