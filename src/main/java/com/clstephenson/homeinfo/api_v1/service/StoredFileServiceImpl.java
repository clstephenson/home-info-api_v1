package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
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

    private final MyLogger LOGGER = new MyLogger(StoredFileServiceImpl.class);

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
    public List<StoredFile> findByCategoryAndCategoryItemId(StoredFile.FileCategory category, long categoryItemId) {
        return StreamSupport.stream(storedFileRepository.findAllByCategoryAndCategoryItemId(category, categoryItemId)
                .spliterator(), false)
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
        String userUuid = storedFile.getProperty().getUser().getUuid();
        UploadFileResponse response = fileStorageService.storeFile(file, storedFile.getUuid(), userUuid);
        storedFileRepository.save(storedFile);
        return response;
    }

    @Override
    public boolean delete(StoredFile storedFile) {
        String userUuid = storedFile.getProperty().getUser().getUuid();
        if (fileStorageService.deleteFileFromStorage(storedFile.getUuid(), userUuid)) {
            storedFileRepository.deleteById(storedFile.getId());
            LOGGER.info(String.format("Deleted file '%s' with UUID '%s'", storedFile.getOriginalFileName(), storedFile.getUuid()));
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean deleteAllByPropertyId(long propertyId) {
        boolean returnValue = true;
        List<StoredFile> storedFiles = findByPropertyId(propertyId);
        for (StoredFile storedFile : storedFiles) {
            if (delete(storedFile) == false) {
                returnValue = false;
            }
        }
        return returnValue;
    }

    @Override
    public boolean deleteAllByCategoryAndCategoryItemId(StoredFile.FileCategory category, long categoryItemId) {
        boolean returnValue = true;
        List<StoredFile> storedFiles = findByCategoryAndCategoryItemId(category, categoryItemId);
        for (StoredFile storedFile : storedFiles) {
            if (delete(storedFile) == false) {
                returnValue = false;
            }
        }
        return returnValue;
    }

    @Override
    public boolean existsById(long id) {
        return storedFileRepository.existsById(id);
    }
}
