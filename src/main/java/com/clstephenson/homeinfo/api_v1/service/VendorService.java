package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Vendor;

import java.util.List;
import java.util.Optional;

public interface VendorService {
    List<Vendor> getAll();

    List<Vendor> findByUserId(long userId);

    Optional<Vendor> findById(long id);

    Vendor save(Vendor vendor);

    void deleteById(long id);

    boolean existsById(long id);
}
