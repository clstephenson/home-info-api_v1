package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.model.Window;
import org.springframework.data.repository.CrudRepository;

public interface WindowRespository extends CrudRepository<Window, Long> {
}
