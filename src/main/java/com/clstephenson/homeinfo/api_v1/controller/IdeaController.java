package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.IdeaNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Idea;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.service.IdeaService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/idea/property/{propertyId}")
    List<Idea> getAllIdeasByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            return ideaService.findByPropertyId(propertyId);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/idea/property/{propertyId}")
    Idea createIdeaWithPropertyId(@Valid @RequestBody Idea newIdea, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newIdea.setProperty(property);
        return ideaService.save(newIdea);
    }

    @GetMapping("/idea/{ideaId}")
    Idea getIdeaById(@PathVariable long ideaId) {
        return ideaService.findById(ideaId)
                .orElseThrow(() -> new IdeaNotFoundException(ideaId));
    }

    @PutMapping("/idea/{ideaId}")
    Idea updateIdea(@PathVariable long ideaId, @Valid Idea ideaRequest) {
        return ideaService.findById(ideaId).map(idea -> {
            // do not allow updates to property field of idea after created
            idea.setDescription(ideaRequest.getDescription());
            idea.setNotes(ideaRequest.getNotes());
            return ideaService.save(idea);
        }).orElseThrow(() -> new IdeaNotFoundException(ideaId));
    }

    @DeleteMapping("/idea/{ideaId}")
    ResponseEntity<?> deleteIdea(@PathVariable long ideaId) {
        return ideaService.findById(ideaId).map(idea -> {
            ideaService.deleteById(ideaId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new IdeaNotFoundException(ideaId));
    }
}
