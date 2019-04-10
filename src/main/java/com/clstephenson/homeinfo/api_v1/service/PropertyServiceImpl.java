package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public List<Property> getAll() {
        return StreamSupport.stream(propertyRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> findByUserId(long userId) {
        return StreamSupport.stream(propertyRepository.findByUserId(userId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Property> findById(long id) {
        return propertyRepository.findById(id);
    }

    @Override
    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public void deleteById(long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return propertyRepository.existsById(id);
    }
}
