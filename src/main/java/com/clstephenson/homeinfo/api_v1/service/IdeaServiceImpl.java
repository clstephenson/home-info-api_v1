package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Idea;
import com.clstephenson.homeinfo.api_v1.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IdeaServiceImpl implements IdeaService {

    @Autowired
    IdeaRepository ideaRepository;

    @Override
    public List<Idea> getAll() {
        return StreamSupport.stream((ideaRepository.findAll().spliterator()), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Idea> findByPropertyId(long propertyId) {
        return StreamSupport.stream(ideaRepository.findAllByPropertyId(propertyId).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Idea> findById(long id) {
        return ideaRepository.findById(id);
    }

    @Override
    public Idea save(Idea idea) {
        return ideaRepository.save(idea);
    }

    @Override
    public void deleteById(long id) {
        ideaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return ideaRepository.existsById(id);
    }
}
