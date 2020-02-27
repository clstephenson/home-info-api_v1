package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.Idea;
import org.springframework.data.repository.CrudRepository;

public interface IdeaRepository extends CrudRepository<Idea, Long> {

    Iterable<Idea> findAllByPropertyId(long propertyId);
}
