package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.configproperty.FileStorageProperties;
import com.clstephenson.homeinfo.api_v1.exception.FileStorageException;
import com.clstephenson.homeinfo.api_v1.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Profile("localstore")
@Service("LocalFileStorageService")
public class LocalFileStorageService implements FileStorageService {

    private final MyLogger LOGGER = new MyLogger(LocalFileStorageService.class);
    private final Path fileStorageLocation;

    @Autowired
    public LocalFileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocalUploadDirectory())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            final String message = String.format("Could not create local directory for uploads: %s", fileStorageLocation);
            throw new FileStorageException(message, ex);
        }
    }

    @Override
    public UploadFileResponse storeFile(MultipartFile file, String targetFileName, String targetFolderName) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Security check
            if (fileName.contains("..")) {
                final String message = String.format("Filename contains invalid path sequence: %s", fileName);
                throw new FileStorageException(message);
            }

            // copy file to the target location (replaces existing file with same name)
            Path targetLocation = this.fileStorageLocation.resolve(targetFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new UploadFileResponse(fileName, targetFileName, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            final String message = String.format("Could not store file '%s' with target filename '%s", fileName, targetFileName);
            throw new FileStorageException(message, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String sourceFileName, String sourceFolderName) {
        try {
            Path uuidPath = this.fileStorageLocation.resolve(sourceFileName).normalize();

            Resource resource = new UrlResource(uuidPath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new StoredFileNotFoundException("File not found " + sourceFileName);
            }
        } catch (MalformedURLException ex) {
            throw new StoredFileNotFoundException("File not found " + sourceFileName, ex);
        }
    }

    @Override
    public boolean deleteFileFromStorage(String targetFileName, String targetFolderName) {
        try {
            Path uuidPath = this.fileStorageLocation.resolve(targetFileName).normalize();
            Files.delete(uuidPath);
            return true;
        } catch (IOException ex) {
            final String message = String.format("File could not be deleted '%s'", targetFileName);
            throw new StoredFileNotFoundException(message, ex);
        }
    }
}
