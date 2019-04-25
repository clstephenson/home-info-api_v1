package com.clstephenson.homeinfo.api_v1.controller.rest;

import com.clstephenson.homeinfo.api_v1.exception.LocationNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.model.LocationList;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.service.LocationService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LocationRestController {

    @Autowired
    LocationService locationService;

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/apiv1/property/{propertyId}/locations")
    ResponseEntity<LocationList> getAllLocationsByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            LocationList locationList = new LocationList();
            locationService.findByPropertyId(propertyId).forEach(location -> locationList.getLocations().add(location));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(locationList);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/property/{propertyId}/location")
    Location createLocationWithPropertyId(@Valid @RequestBody Location newLocation, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newLocation.setProperty(property);
        return locationService.save(newLocation);
    }

    @GetMapping("/apiv1/property/{propertyId}/location/{locationId}")
    Location getLocationById(@PathVariable long propertyId, @PathVariable long locationId) {
        if (propertyService.existsById(propertyId)) {
            return locationService.findById(locationId)
                    .orElseThrow(() -> new LocationNotFoundException(locationId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @PutMapping("/apiv1/property/{propertyId}/location/{locationId}")
    Location updateLocation(@PathVariable long propertyId, @PathVariable long locationId, @Valid @RequestBody Location locationRequest) {
        if (propertyService.existsById(propertyId)) {
            return locationService.findById(locationId).map(location -> {
                // do not allow updates to property field of location after created
                location.setName(locationRequest.getName());
                location.setDimensions(locationRequest.getDimensions());
                location.setNotes(locationRequest.getNotes());
                return locationService.save(location);
            }).orElseThrow(() -> new LocationNotFoundException(locationId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @DeleteMapping("/apiv1/property/{propertyId}/location/{locationId}")
    ResponseEntity<?> deleteLocation(@PathVariable long propertyId, @PathVariable long locationId) {
        if (propertyService.existsById(propertyId)) {
            return locationService.findById(locationId).map(location -> {
                locationService.deleteById(locationId);
                storedFileService.deleteAllByCategoryAndCategoryItemId(StoredFile.FileCategory.LOCATION, locationId);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new LocationNotFoundException(locationId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }
}
