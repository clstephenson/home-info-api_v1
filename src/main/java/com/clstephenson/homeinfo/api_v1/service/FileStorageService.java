package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    UploadFileResponse storeFile(MultipartFile file, String path);

    Resource loadFileAsResource(String uuid);

    boolean deleteFileFromStorage(String uuid);
}
