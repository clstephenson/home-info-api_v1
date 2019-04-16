package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    UploadFileResponse storeFile(MultipartFile file, String targetFileName, String targetFolderName);

    Resource loadFileAsResource(String sourceFileName, String sourceFolderName);

    boolean deleteFileFromStorage(String targetFileName, String targetFolderName);
}
