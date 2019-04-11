package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LocationService {
    List<Location> getAll();

    List<Location> findByPropertyId(long propertyId);

    Optional<Location> findById(long id);

    Location save(Location location);

    void deleteById(long id);

    boolean existsById(long id);
}
