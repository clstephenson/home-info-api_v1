package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TaskService {
    List<Task> getAll();

    List<Task> findByPropertyId(long propertyId);

    Optional<Task> findById(long id);

    Task save(Task task);

    void deleteById(long id);

    boolean existsById(long id);
}
