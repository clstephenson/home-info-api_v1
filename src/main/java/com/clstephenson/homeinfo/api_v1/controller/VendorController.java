package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.UserNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.VendorNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import com.clstephenson.homeinfo.api_v1.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class VendorController {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @GetMapping("/vendor/user/{userId}")
    Iterable<Vendor> getAllVendorsByUserId(@PathVariable Long userId) {
        if (userService.existsById(userId)) {
            return vendorService.findByUserId(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/vendor/user/{userId}")
    Vendor createVendorWithUserId(@Valid @RequestBody Vendor newVendor, @PathVariable Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newVendor.setUser(user);
        return vendorService.save(newVendor);
    }

    @GetMapping("/vendor/{vendorId}")
    Vendor getPropertyById(@PathVariable Long vendorId) {
        return vendorService.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(vendorId));
    }

    @PutMapping("/vendor/{vendorId}")
    Vendor updateVendor(@PathVariable Long vendorId, @Valid @RequestBody Vendor vendorRequest) {
        return vendorService.findById(vendorId)
                .map(vendor -> {
                    vendor.setName(vendorRequest.getName());
                    vendor.setEmail(vendorRequest.getEmail());
                    vendor.setPhone(vendorRequest.getPhone());
                    vendor.setWebsite(vendorRequest.getWebsite());
                    vendor.setNotes(vendorRequest.getNotes());
                    vendor.setUser(vendorRequest.getUser());
                    return vendorService.save(vendor);
                }).orElseThrow(() -> new VendorNotFoundException(vendorId));
    }

    @DeleteMapping("/vendor/{vendorId}")
    ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {
        return vendorService.findById(vendorId).map(vendor -> {
            vendorService.deleteById(vendorId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new VendorNotFoundException(vendorId));
    }

}
