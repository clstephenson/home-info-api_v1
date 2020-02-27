package com.clstephenson.homeinfo.api.controller;

import com.clstephenson.homeinfo.api.configproperty.AwsProperties;
import com.clstephenson.homeinfo.api.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.logging.MyLogger;
import com.clstephenson.homeinfo.model.StoredFile;
import com.clstephenson.homeinfo.model.StoredFileList;
import com.clstephenson.homeinfo.model.UploadFileResponse;
import com.clstephenson.homeinfo.api.service.FileStorageService;
import com.clstephenson.homeinfo.api.service.PropertyService;
import com.clstephenson.homeinfo.api.service.StoredFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class StoredFileRestController {

    private final MyLogger LOGGER = new MyLogger(StoredFileRestController.class);

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AwsProperties awsProperties;

    @GetMapping("/apiv1/property/{propertyId}/storedFiles")
    ResponseEntity<StoredFileList> getAllStoredFilesByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            StoredFileList storedFileList = new StoredFileList();
            storedFileService.findByPropertyId(propertyId).forEach(storedFile -> storedFileList.getStoredFiles().add(storedFile));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(storedFileList);
        } else {
            final String message = String.format("Could not get file list: Property with id '%d' could not be found", propertyId);
            throw new PropertyNotFoundException(message);
        }
    }

    @GetMapping("/apiv1/storedFiles/{category}/{categoryItemId}")
    ResponseEntity<StoredFileList> getAllStoredFilesByCategoryAndCategoryItemId(@PathVariable String category,
                                                                                @PathVariable long categoryItemId) {
        StoredFileList storedFileList = new StoredFileList();
        storedFileService.findByCategoryAndCategoryItemId(StoredFile.FileCategory.valueOf(category), categoryItemId).
                forEach(storedFile -> storedFileList.getStoredFiles().add(storedFile));
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(storedFileList);
    }

    @GetMapping("/apiv1/storedFile/{fileId}")
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
            LOGGER.info(String.format(
                    "Could not determine contentType of file '%s'. Falling back to defaults", resource.getFilename()));
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/property/{propertyId}/storedFile/{fileCategory}/{categoryItemId}")
    public UploadFileResponse uploadFile(@PathVariable long propertyId,
                                         @PathVariable String fileCategory,
                                         @PathVariable long categoryItemId,
                                         @RequestBody MultipartFile file) {
        try {
            return propertyService.findById(propertyId).map(property -> {
                StoredFile storedFile = new StoredFile();
                storedFile.setProperty(property);
                storedFile.setOriginalFileName(file.getOriginalFilename());
                storedFile.setContentType(file.getContentType());
                storedFile.setCategory(StoredFile.FileCategory.valueOf(fileCategory));
                storedFile.setCategoryItemId(categoryItemId);
                return storedFileService.save(storedFile, file);
            }).orElseThrow(() -> {
                final String message = String.format("File not uploaded: Property with id '%d' could not be found", propertyId);
                return new PropertyNotFoundException(message);
            });
        } catch (IllegalArgumentException e) {
            final String message = String.format(
                    "Illegal Argument: Path variable 'category=%s' could not be converted to a FileCategory", fileCategory);
            throw new StoredFileNotFoundException(message);
        }
    }

    @DeleteMapping("/apiv1/storedFile/{storedFileId}")
    ResponseEntity<?> deleteStoredFile(@PathVariable long storedFileId) {
        try {
            return storedFileService.findById(storedFileId)
                    .map(storedFile -> {
                        if (storedFileService.delete(storedFile)) {
                            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                        } else {
                            throw new StoredFileNotFoundException(storedFileId);
                        }
                    })
                    .orElseThrow(() -> new StoredFileNotFoundException(storedFileId));
        } catch (StoredFileNotFoundException e) {
            final String message = String.format("The file with id '%d' could not be deleted", storedFileId);
            throw new StoredFileNotFoundException(message, e);
        }
    }
}
