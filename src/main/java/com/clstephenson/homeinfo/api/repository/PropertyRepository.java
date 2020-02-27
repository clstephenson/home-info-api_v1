package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, Long> {

    Iterable<Property> findByUserId(Long userId);

}
