package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import org.springframework.data.repository.CrudRepository;

public interface StoredFileRepository extends CrudRepository<StoredFile, Long> {
}
