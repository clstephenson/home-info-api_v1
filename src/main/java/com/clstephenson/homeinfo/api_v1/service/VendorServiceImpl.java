package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public List<Vendor> getAll() {
        return StreamSupport.stream(vendorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vendor> findByUserId(long userId) {
        return StreamSupport.stream(vendorRepository.findByUserId(userId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Vendor> findById(long id) {
        return vendorRepository.findById(id);
    }

    @Override
    public Vendor save(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public void deleteById(long id) {
        vendorRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return vendorRepository.existsById(id);
    }
}
