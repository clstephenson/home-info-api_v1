package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.UserNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private UserService userService;

    // Find All
    @GetMapping("/property")
    Iterable<Property> getAllProperties() {
        return propertyService.getAll();
    }

    // Find all properties by user ID
    @GetMapping("/property/user/{userId}")
    Iterable<Property> getAllPropertiesByUserId(@PathVariable Long userId) {
        if (userService.existsById(userId)) {
            return propertyService.findByUserId(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    // Save
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/property/user/{userId}")
    Property createPropertyWithUserId(@Valid @RequestBody Property newProperty, @PathVariable Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newProperty.setUser(user);
        return propertyService.save(newProperty);
    }

    // Find by ID
    @GetMapping("/property/{propertyId}")
    Property getPropertyById(@PathVariable Long propertyId) {
        return propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    // Update Property
    @PutMapping("/property/{propertyId}")
    Property updateProperty(@PathVariable Long propertyId, @Valid @RequestBody Property propertyRequest) {
        return propertyService.findById(propertyId)
                .map(property -> {
                    property.setName(propertyRequest.getName());
                    property.setAddress(propertyRequest.getAddress());
                    property.setSquareFootage(propertyRequest.getSquareFootage());
                    property.setYearBuilt(propertyRequest.getYearBuilt());
                    property.setUser(propertyRequest.getUser());
                    return propertyService.save(property);
                }).orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    @DeleteMapping("/property/{propertyId}")
    ResponseEntity<?> deleteProperty(@PathVariable Long propertyId) {
        return propertyService.findById(propertyId).map(property -> {
            propertyService.deleteById(propertyId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

}
