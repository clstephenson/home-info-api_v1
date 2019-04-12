package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import com.clstephenson.homeinfo.api_v1.repository.StoredFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StoredFileServiceImpl implements StoredFileService {

    @Autowired
    StoredFileRepository storedFileRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Override
    public List<StoredFile> getAll() {
        return StreamSupport.stream(storedFileRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoredFile> findByPropertyId(long propertyId) {
        return StreamSupport.stream(storedFileRepository.findAllByPropertyId(propertyId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StoredFile> findById(long id) {
        return storedFileRepository.findById(id);
    }

    @Override
    public UploadFileResponse save(StoredFile storedFile, MultipartFile file) {
        if (storedFile.getUuid() == null || storedFile.getUuid().isEmpty()) {
            storedFile.setUuid(UUID.randomUUID().toString());
        }
        UploadFileResponse response = fileStorageService.storeFile(file, storedFile.getUuid());
        storedFileRepository.save(storedFile);
        return response;
    }

    @Override
    public boolean delete(StoredFile storedFile) {
        if (fileStorageService.deleteFileFromStorage(storedFile.getUuid())) {
            storedFileRepository.deleteById(storedFile.getId());
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean existsById(long id) {
        return storedFileRepository.existsById(id);
    }
}
