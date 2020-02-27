package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.Vendor;
import org.springframework.data.repository.CrudRepository;

public interface VendorRepository extends CrudRepository<Vendor, Long> {

    Iterable<Vendor> findByUserId(Long userId);
}
