package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.repository.StoredFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StoredFileServiceImpl implements StoredFileService {

    @Autowired
    StoredFileRepository storedFileRepository;

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
    public Optional<StoredFile> findById(long id) {
        return storedFileRepository.findById(id);
    }

    @Override
    public StoredFile save(StoredFile storedFile) {
        return storedFileRepository.save(storedFile);
    }

    @Override
    public void deleteById(long id) {
        storedFileRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return storedFileRepository.existsById(id);
    }
}
