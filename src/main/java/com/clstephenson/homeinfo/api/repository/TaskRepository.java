package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Iterable<Task> findAllByPropertyId(long propertyId);

}
