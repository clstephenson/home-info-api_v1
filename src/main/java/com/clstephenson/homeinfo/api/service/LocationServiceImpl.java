package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.Location;
import com.clstephenson.homeinfo.api.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    FeatureService featureService;

    @Override
    public List<Location> getAll() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Location> findByPropertyId(long propertyId) {
        return StreamSupport.stream(locationRepository.findAllByPropertyId(propertyId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Location> findById(long id) {
        return locationRepository.findById(id);
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public void deleteByPropertyId(long propertyId) {
        for (Location location : findByPropertyId(propertyId)) {
            deleteById(location.getId());
        }
    }

    @Override
    public void deleteById(long id) {
        // set location field to null on any related features
        featureService.findByLocationId(id).forEach(feature -> {
            feature.setLocation(null);
            featureService.save(feature);
        });

        locationRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return locationRepository.existsById(id);
    }
}
