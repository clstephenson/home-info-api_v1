package com.clstephenson.homeinfo.api.controller;

import com.clstephenson.homeinfo.api.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api.exception.UserNotFoundException;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.PropertyList;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.service.LocationService;
import com.clstephenson.homeinfo.api.service.PropertyService;
import com.clstephenson.homeinfo.api.service.StoredFileService;
import com.clstephenson.homeinfo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PropertyRestController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private StoredFileService storedFileService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    // Find All
    @GetMapping("/apiv1/properties")
    List<Property> getAllProperties() {
        return propertyService.getAll();
    }

    // Find all properties by user ID
    @GetMapping("/apiv1/properties/user/{userId}")
    ResponseEntity<PropertyList> getAllPropertiesByUserId(@PathVariable long userId) {
        if (userService.existsById(userId)) {
            PropertyList propertyList = new PropertyList();
            propertyService.findByUserId(userId).forEach(property -> propertyList.getProperties().add(property));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(propertyList);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    // Save
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/property/user/{userId}")
    Property createPropertyWithUserId(@Valid @RequestBody Property newProperty, @PathVariable long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        newProperty.setUser(user);
        return propertyService.save(newProperty);
    }

    // Find by ID
    @GetMapping("/apiv1/property/{propertyId}")
    Property getPropertyById(@PathVariable long propertyId) {
        return propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    // Update Property
    @PutMapping("/apiv1/property/{propertyId}")
    Property updateProperty(@PathVariable long propertyId, @Valid @RequestBody Property propertyRequest) {
        return propertyService.findById(propertyId)
                .map(property -> {
                    property.setName(propertyRequest.getName());
                    property.setAddress(propertyRequest.getAddress());
                    property.setSquareFootage(propertyRequest.getSquareFootage());
                    property.setYearBuilt(propertyRequest.getYearBuilt());
                    //property.setUser(propertyRequest.getUser());
                    return propertyService.save(property);
                }).orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    @DeleteMapping("/apiv1/property/{propertyId}")
    ResponseEntity<?> deleteProperty(@PathVariable long propertyId) {
        return propertyService.findById(propertyId).map(property -> {
            storedFileService.deleteAllByPropertyId(propertyId);
            locationService.deleteByPropertyId(propertyId);
            propertyService.deleteById(propertyId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }).orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

}
