package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.Task;
import com.clstephenson.homeinfo.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository repository;

    @Override
    public List<Task> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByPropertyId(long propertyId) {
        return StreamSupport.stream(repository.findAllByPropertyId(propertyId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Task> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }
}
