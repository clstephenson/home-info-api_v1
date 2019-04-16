package com.clstephenson.homeinfo.api_v1.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.clstephenson.homeinfo.api_v1.configproperty.AwsProperties;
import com.clstephenson.homeinfo.api_v1.exception.FileStorageException;
import com.clstephenson.homeinfo.api_v1.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Profile("s3store")
@Service("AwsS3FileStorageService")
public class AwsS3FileStorageService implements FileStorageService {

    private final String BUCKET_NAME;
    private AmazonS3 s3Client;

    @Autowired
    public AwsS3FileStorageService(AwsProperties awsProperties) {
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.fromName(awsProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        awsProperties.getCredentialsAccessKey(),
                        awsProperties.getCredentialsSecretKey())))
                .build();
        BUCKET_NAME = awsProperties.getBucketName();
    }

    @Override
    public UploadFileResponse storeFile(MultipartFile file, String targetFileName, String targetFolderName) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Security check
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            s3Client.putObject(
                    BUCKET_NAME,
                    getS3Key(targetFolderName, targetFileName),
                    file.getInputStream(),
                    metadata);
            return new UploadFileResponse(fileName, targetFileName, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file" + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String sourceFileName, String sourceFolderName) {
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, getS3Key(sourceFolderName, sourceFileName));
        Resource resource = new InputStreamResource(s3Object.getObjectContent());
        if (resource.exists()) {
            return resource;
        } else {
            throw new StoredFileNotFoundException("File not found " + sourceFileName);
        }
    }

    @Override
    public boolean deleteFileFromStorage(String targetFileName, String targetFolderName) {
        String s3Key = getS3Key(targetFolderName, targetFileName);
        if (s3Client.doesObjectExist(BUCKET_NAME, s3Key)) {
            try {
                s3Client.deleteObject(BUCKET_NAME, s3Key);
                return !s3Client.doesObjectExist(BUCKET_NAME, s3Key);
            } catch (SdkClientException e) {
                throw new FileStorageException("File could not be deleted " + s3Key, e);
            }
        } else {
            throw new StoredFileNotFoundException("File not found in storage target " + s3Key);
        }
    }

    private String getS3Key(String folderName, String fileName) {
        return String.join("/", folderName, fileName);
    }
}
