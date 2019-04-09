package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.model.Material;
import org.springframework.data.repository.CrudRepository;

public interface MaterialRepository extends CrudRepository<Material, Long> {
}
