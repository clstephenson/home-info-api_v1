package com.clstephenson.homeinfo.api.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.clstephenson.homeinfo.api.configproperty.AwsProperties;
import com.clstephenson.homeinfo.api.exception.FileStorageException;
import com.clstephenson.homeinfo.api.exception.StoredFileNotFoundException;
import com.clstephenson.homeinfo.logging.MyLogger;
import com.clstephenson.homeinfo.model.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Profile("s3store")
@Service("AwsS3FileStorageService")
public class AwsS3FileStorageService implements FileStorageService {

    private final MyLogger LOGGER = new MyLogger(AwsS3FileStorageService.class);
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
            final String message = String.format("Filename contains invalid path sequence: %s", fileName);
            throw new FileStorageException(message);
        }
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String s3Key = getS3Key(targetFolderName, targetFileName);
            s3Client.putObject(BUCKET_NAME, s3Key, file.getInputStream(), metadata);

            LOGGER.info(String.format("Uploaded [%s] to S3 bucket [%s]", s3Key, BUCKET_NAME));
            return new UploadFileResponse(fileName, targetFileName, file.getContentType(), file.getSize());
        } catch (Exception ex) {
            final String message = String.format("Could not store file '%s' with target filename '%s", fileName, targetFileName);
            throw new FileStorageException(message, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String sourceFileName, String sourceFolderName) {
        String s3Key = getS3Key(sourceFolderName, sourceFileName);
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, s3Key);
        Resource resource = new InputStreamResource(s3Object.getObjectContent());
        if (resource.exists()) {
            LOGGER.info(String.format("Downloaded File [%s] from S3 bucket [%s]", s3Key, BUCKET_NAME));
            return resource;
        } else {
            final String message = String.format("Could not find File [%s] in S3 bucket [%s]", s3Key, BUCKET_NAME);
            throw new StoredFileNotFoundException(message);
        }
    }

    @Override
    public boolean deleteFileFromStorage(String targetFileName, String targetFolderName) {
        String s3Key = getS3Key(targetFolderName, targetFileName);
        if (s3Client.doesObjectExist(BUCKET_NAME, s3Key)) {
            try {
                s3Client.deleteObject(BUCKET_NAME, s3Key);
                if (s3Client.doesObjectExist(BUCKET_NAME, s3Key)) {
                    return false;
                } else {
                    LOGGER.info(String.format("Deleted [%s] from S3 bucket [%s]", s3Key, BUCKET_NAME));
                    return true;
                }
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
