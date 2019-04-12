package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.repository.LocationRepository;
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
    public void deleteById(long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return locationRepository.existsById(id);
    }
}
