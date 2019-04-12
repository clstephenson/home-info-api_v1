package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
    List<Property> getAll();

    List<Property> findByUserId(long userId);

    Optional<Property> findById(long id);

    Property save(Property property);

    void deleteById(long id);

    boolean existsById(long id);
}
