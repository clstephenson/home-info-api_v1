package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.StoredFile;

import java.util.List;
import java.util.Optional;

public interface StoredFileService {
    List<StoredFile> getAll();

    List<StoredFile> findByPropertyId(long propertyId);

    Optional<StoredFile> findById(long id);

    StoredFile save(StoredFile storedFile);

    void deleteById(long id);

    boolean existsById(long id);
}
