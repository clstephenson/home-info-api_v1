package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<Location> getAll();

    List<Location> findByPropertyId(long propertyId);

    Optional<Location> findById(long id);

    Location save(Location location);

    void deleteByPropertyId(long propertyId);

    void deleteById(long id);

    boolean existsById(long id);
}
