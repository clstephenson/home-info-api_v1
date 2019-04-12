package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.configproperty.FileStorageProperties;
import com.clstephenson.homeinfo.api_v1.exception.FileStorageException;
import com.clstephenson.homeinfo.api_v1.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public LocalFileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocalUploadDirectory())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public UploadFileResponse storeFile(MultipartFile file, String uuid) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Security check
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // copy file to the target location (replaces existing file with same name)
            Path targetLocation = this.fileStorageLocation.resolve(uuid);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new UploadFileResponse(fileName, uuid, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file" + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String uuid) {
        try {
            Path uuidPath = this.fileStorageLocation.resolve(uuid).normalize();

            Resource resource = new UrlResource(uuidPath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new StoredFileNotFoundException("File not found " + uuid);
            }
        } catch (MalformedURLException ex) {
            throw new StoredFileNotFoundException("File not found " + uuid, ex);
        }
    }

    @Override
    public boolean deleteFileFromStorage(String uuid) {
        try {
            Path uuidPath = this.fileStorageLocation.resolve(uuid).normalize();
            Files.delete(uuidPath);
            return true;
        } catch (IOException ex) {
            throw new StoredFileNotFoundException("File could not be deleted " + uuid, ex);
        }
    }
}
