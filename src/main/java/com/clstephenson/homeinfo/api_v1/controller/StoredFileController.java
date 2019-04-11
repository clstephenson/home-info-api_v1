package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// todo support uploads and downloads
@RestController
public class StoredFileController {

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/storedFile/property/{propertyId}")
    List<StoredFile> getAllStoredFilesByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            return storedFileService.findByPropertyId(propertyId);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @PostMapping("/storedFile/property/{propertyId}")
    ResponseEntity<?> createStoredFileWithPropertyId(@Valid @RequestBody StoredFile newStoredFile, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/storedFile/{storedFileId}")
    ResponseEntity<?> getStoredFileById(@PathVariable long storedFileId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PutMapping("/storedFile/{storedFileId}")
    ResponseEntity<?> updateStoredFile(@PathVariable long storedFileId, @Valid StoredFile storedFileRequest) {
        return storedFileService.findById(storedFileId)
                .map(storedFile -> ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build())
                .orElseThrow(() -> new StoredFileNotFoundException(storedFileId));
    }

    @DeleteMapping("/storedFile/{storedFileId}")
    ResponseEntity<?> deleteStoredFile(@PathVariable long storedFileId) {
        return storedFileService.findById(storedFileId)
                .map(storedFile -> ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build())
                .orElseThrow(() -> new StoredFileNotFoundException(storedFileId));
    }
}
