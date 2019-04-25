package com.clstephenson.homeinfo.api_v1.controller.rest;

import com.clstephenson.homeinfo.api_v1.exception.UserNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.VendorNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.model.VendorList;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import com.clstephenson.homeinfo.api_v1.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class VendorRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @GetMapping("/apiv1/user/{userId}/vendors")
    ResponseEntity<VendorList> getAllVendorsByUserId(@PathVariable Long userId) {
        if (userService.existsById(userId)) {
            VendorList vendorList = new VendorList();
            vendorService.findByUserId(userId).forEach(vendor -> vendorList.getVendors().add(vendor));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(vendorList);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/user/{userId}/vendor")
    Vendor createVendorWithUserId(@Valid @RequestBody Vendor newVendor, @PathVariable Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newVendor.setUser(user);
        return vendorService.save(newVendor);
    }

    @GetMapping("/apiv1/vendor/{vendorId}")
    ResponseEntity<Vendor> getVendorById(@PathVariable Long vendorId) {
        Vendor vendor = vendorService.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(vendorId));

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(vendor);
    }

    @PutMapping("/apiv1/vendor/{vendorId}")
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

    @DeleteMapping("/apiv1/vendor/{vendorId}")
    ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {
        return vendorService.findById(vendorId).map(vendor -> {
            vendorService.deleteById(vendorId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new VendorNotFoundException(vendorId));
    }

}
