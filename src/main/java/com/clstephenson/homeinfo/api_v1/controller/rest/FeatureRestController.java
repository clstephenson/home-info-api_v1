package com.clstephenson.homeinfo.api_v1.controller.rest;

import com.clstephenson.homeinfo.api_v1.exception.FeatureNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.LocationNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Feature;
import com.clstephenson.homeinfo.api_v1.model.FeatureList;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.service.FeatureService;
import com.clstephenson.homeinfo.api_v1.service.LocationService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FeatureRestController {

    @Autowired
    FeatureService featureService;

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @Autowired
    LocationService locationService;

    @GetMapping("/apiv1/property/{propertyId}/features")
    ResponseEntity<FeatureList> getAllFeaturesByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            FeatureList featureList = new FeatureList();
            featureService.findByPropertyId(propertyId).forEach(feature -> featureList.getFeatures().add(feature));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(featureList);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @GetMapping("/apiv1/property/{propertyId}/feature/{featureId}")
    Feature getFeatureByIdAndPropertyId(@PathVariable long propertyId, @PathVariable long featureId) {
        if (propertyService.existsById(propertyId)) {
            return featureService.findById(featureId)
                    .orElseThrow(() -> new FeatureNotFoundException(featureId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @GetMapping("/apiv1/property/{propertyId}/location/{locationId}/feature")
    List<Feature> getFeaturesByPropertyIdAndLocationId(@PathVariable long propertyId, @PathVariable long locationId) {
        if (propertyService.existsById(propertyId)) {
            if (locationService.existsById(locationId)) {
                return featureService.findByPropertyIdAndLocationId(propertyId, locationId);
            } else {
                throw new LocationNotFoundException(locationId);
            }
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/property/{propertyId}/feature")
    Feature createFeatureWithPropertyId(@Valid @RequestBody Feature newFeature, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newFeature.setProperty(property);
        return featureService.save(newFeature);
    }

    @PutMapping("/apiv1/property/{propertyId}/feature/{featureId}")
    Feature updateFeature(@PathVariable long propertyId, @PathVariable long featureId, @Valid @RequestBody Feature featureRequest) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        return featureService.findById(featureId).map(feature -> {
            feature.setName(featureRequest.getName());
            feature.setNotes(featureRequest.getNotes());
            feature.setLocation(featureRequest.getLocation());
            feature.setType(featureRequest.getType());
            //feature.setProperty(property);
            return featureService.save(feature);
        }).orElseThrow(() -> new FeatureNotFoundException(featureId));
    }

    @DeleteMapping("/apiv1/property/{propertyId}/feature/{featureId}")
    ResponseEntity<?> deleteFeature(@PathVariable long propertyId, @PathVariable long featureId) {
        if (propertyService.existsById(propertyId)) {
            return featureService.findById(featureId).map(feature -> {
                featureService.deleteById(featureId);
                storedFileService.deleteAllByCategoryAndCategoryItemId(StoredFile.FileCategory.FEATURE, featureId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }).orElseThrow(() -> new FeatureNotFoundException(featureId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }
}
