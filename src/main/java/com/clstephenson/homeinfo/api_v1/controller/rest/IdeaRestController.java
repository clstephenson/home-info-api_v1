package com.clstephenson.homeinfo.api_v1.controller.rest;

import com.clstephenson.homeinfo.api_v1.exception.IdeaNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Idea;
import com.clstephenson.homeinfo.api_v1.model.IdeaList;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.service.IdeaService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class IdeaRestController {

    @Autowired
    IdeaService ideaService;

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/apiv1/property/{propertyId}/ideas")
    ResponseEntity<IdeaList> getAllIdeasByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            IdeaList ideaList = new IdeaList();
            ideaService.findByPropertyId(propertyId).forEach(idea -> ideaList.getIdeas().add(idea));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(ideaList);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @GetMapping("/apiv1/property/{propertyId}/idea/{ideaId}")
    Idea getIdeaByIdAndPropertyId(@PathVariable long propertyId, @PathVariable long ideaId) {
        if (propertyService.existsById(propertyId)) {
            return ideaService.findById(ideaId)
                    .orElseThrow(() -> new IdeaNotFoundException(ideaId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/property/{propertyId}/idea")
    Idea createIdeaWithPropertyId(@Valid @RequestBody Idea newIdea, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newIdea.setProperty(property);
        return ideaService.save(newIdea);
    }

    @PutMapping("/apiv1/property/{propertyId}/idea/{ideaId}")
    Idea updateIdea(@PathVariable long propertyId, @PathVariable long ideaId, @Valid @RequestBody Idea ideaRequest) {
        return ideaService.findById(ideaId).map(idea -> {
            // do not allow updates to property field of idea after created
            idea.setDescription(ideaRequest.getDescription());
            idea.setNotes(ideaRequest.getNotes());
            return ideaService.save(idea);
        }).orElseThrow(() -> new IdeaNotFoundException(ideaId));
    }

    @DeleteMapping("/apiv1/property/{propertyId}/idea/{ideaId}")
    ResponseEntity<?> deleteIdea(@PathVariable long propertyId, @PathVariable long ideaId) {
        if (propertyService.existsById(propertyId)) {
            return ideaService.findById(ideaId).map(idea -> {
                ideaService.deleteById(ideaId);
                storedFileService.deleteAllByCategoryAndCategoryItemId(StoredFile.FileCategory.IDEA, ideaId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }).orElseThrow(() -> new IdeaNotFoundException(ideaId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }
}
