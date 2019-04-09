package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.model.Maintenance;
import org.springframework.data.repository.CrudRepository;

public interface MaintenanceRepository extends CrudRepository<Maintenance, Long> {
}
