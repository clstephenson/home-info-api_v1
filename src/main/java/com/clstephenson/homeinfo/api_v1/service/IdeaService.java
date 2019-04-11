package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Idea;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IdeaService {
    List<Idea> getAll();

    List<Idea> findByPropertyId(long propertyId);

    Optional<Idea> findById(long id);

    Idea save(Idea idea);

    void deleteById(long id);

    boolean existsById(long id);
}
