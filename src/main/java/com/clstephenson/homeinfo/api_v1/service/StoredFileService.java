package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StoredFileService {
    List<StoredFile> getAll();

    List<StoredFile> findByPropertyId(long propertyId);

    List<StoredFile> findByCategoryAndCategoryItemId(StoredFile.FileCategory category, long categoryItemId);

    Optional<StoredFile> findById(long id);

    UploadFileResponse save(StoredFile storedFile, MultipartFile file);

    boolean delete(StoredFile storedFile);

    boolean deleteAllByCategoryAndCategoryItemId(StoredFile.FileCategory category, long categoryItemId);

    boolean existsById(long id);
}
