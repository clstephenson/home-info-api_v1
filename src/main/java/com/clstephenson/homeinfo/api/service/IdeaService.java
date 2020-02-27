package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.Idea;

import java.util.List;
import java.util.Optional;

public interface IdeaService {
    List<Idea> getAll();

    List<Idea> findByPropertyId(long propertyId);

    Optional<Idea> findById(long id);

    Idea save(Idea idea);

    void deleteById(long id);

    boolean existsById(long id);
}
