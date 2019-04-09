package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
