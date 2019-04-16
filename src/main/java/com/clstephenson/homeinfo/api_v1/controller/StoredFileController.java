package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.configproperty.AwsProperties;
import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import com.clstephenson.homeinfo.api_v1.service.FileStorageService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

// todo support uploads and downloads
@RestController
public class StoredFileController {

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AwsProperties awsProperties;

    @GetMapping("/property/{propertyId}/storedFile")
    public List<StoredFile> getStoredFilesByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            return storedFileService.findByPropertyId(propertyId);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @PostMapping("/property/{propertyId}/storedFile")
    public UploadFileResponse uploadFile(@PathVariable long propertyId, @RequestParam("file") MultipartFile file) {
        return propertyService.findById(propertyId).map(property -> {
            StoredFile storedFile = new StoredFile();
            storedFile.setProperty(property);
            storedFile.setOriginalFileName(file.getOriginalFilename());
            storedFile.setContentType(file.getContentType());
            storedFile.setCategory(StoredFile.FileCategory.DOCUMENT);
            return storedFileService.save(storedFile, file);
        }).orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }

    @GetMapping("/storedFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long fileId, HttpServletRequest request) {
        StoredFile storedFile = storedFileService.findById(fileId)
                .orElseThrow(() -> new StoredFileNotFoundException(fileId));

        String userUuid = storedFile.getProperty().getUser().getUuid();
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(storedFile.getUuid(), userUuid);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            // todo log message "Could not determine file type."
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = storedFile.getContentType() == null ? "application/octet-stream" : storedFile.getContentType();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + storedFile.getOriginalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/storedFile/{storedFileId}")
    ResponseEntity<?> deleteStoredFile(@PathVariable long storedFileId) {
        return storedFileService.findById(storedFileId)
                .map(storedFile -> {
                    if (storedFileService.delete(storedFile)) {
                        return ResponseEntity.ok().build();
                    } else {
                        throw new StoredFileNotFoundException(storedFileId);
                    }
                })
                .orElseThrow(() -> new StoredFileNotFoundException(storedFileId));
    }
}
