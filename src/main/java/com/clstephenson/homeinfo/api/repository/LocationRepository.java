package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {

    Iterable<Location> findAllByPropertyId(long propertyId);

}
