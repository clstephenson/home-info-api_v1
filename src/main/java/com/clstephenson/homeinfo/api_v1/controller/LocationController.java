package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.LocationNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.service.LocationService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LocationController {

    @Autowired
    LocationService locationService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/location/property/{propertyId}")
    List<Location> getAllLocationsByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            return locationService.findByPropertyId(propertyId);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/location/property/{propertyId}")
    Location createLocationWithPropertyId(@Valid @RequestBody Location newLocation, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newLocation.setProperty(property);
        return locationService.save(newLocation);
    }

    @GetMapping("/location/{locationId}")
    Location getLocationById(@PathVariable long locationId) {
        return locationService.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
    }

    @PutMapping("/location/{locationId}")
    Location updateLocation(@PathVariable long locationId, @Valid Location locationRequest) {
        return locationService.findById(locationId).map(location -> {
            // do not allow updates to property field of location after created
            location.setName(locationRequest.getName());
            location.setDimensions(locationRequest.getDimensions());
            location.setNotes(locationRequest.getNotes());
            return locationService.save(location);
        }).orElseThrow(() -> new LocationNotFoundException(locationId));
    }

    @DeleteMapping("/location/{locationId}")
    ResponseEntity<?> deleteLocation(@PathVariable long locationId) {
        return locationService.findById(locationId).map(location -> {
            locationService.deleteById(locationId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new LocationNotFoundException(locationId));
    }
}
