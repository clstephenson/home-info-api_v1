package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.StoredFile;
import org.springframework.data.repository.CrudRepository;

public interface StoredFileRepository extends CrudRepository<StoredFile, Long> {

    Iterable<StoredFile> findAllByPropertyId(long propertyId);

    Iterable<StoredFile> findAllByCategoryAndCategoryItemId(StoredFile.FileCategory category, long categoryItemId);

}
