package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//@Primary
@Service("AwsS3FileStorageService")
public class AwsS3FileStorageService implements FileStorageService {
    @Override
    public UploadFileResponse storeFile(MultipartFile file, String path) {
        return null;
    }

    @Override
    public Resource loadFileAsResource(String uuid) {
        return null;
    }

    @Override
    public boolean deleteFileFromStorage(String uuid) {
        return false;
    }
}
