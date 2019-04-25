package com.clstephenson.homeinfo.api_v1.controller.web;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import com.clstephenson.homeinfo.api_v1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class IdeaController {

    private final MyLogger LOGGER = new MyLogger(IdeaController.class);

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @GetMapping("/ideas")
    public String ideas(Model model, HttpServletRequest request,
                        @RequestParam(value = "user_id") long userId,
                        @RequestParam(value = "property_id") long propertyId) {
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/ideas";
        IdeaList ideaList = restTemplateHelper.getForEntity(IdeaList.class, endpoint, propertyId);
        List<Idea> ideas = ideaList == null ? Collections.emptyList() : ideaList.getIdeas();

        model.addAttribute("userId", userId);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("ideas", ideas);
        return "/ideas";
    }

    @GetMapping("/idea")
    public String idea(Model model, HttpServletRequest request,
                       @RequestParam(value = "idea_id", required = false) Long ideaId,
                       @RequestParam(value = "property_id") Long propertyId,
                       @RequestParam(value = "user_id") Long userId) {
        Idea idea;
        List<StoredFile> files = Collections.emptyList();
        String action;
        if (ideaId != null && ideaId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/idea/{ideaId}";
            idea = restTemplateHelper.getForEntity(Idea.class, endpoint, propertyId, ideaId);
            userId = idea != null ? idea.getProperty().getUser().getId() : 0;

            // get any files associated with the feature
            endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/storedFiles/{category}/{categoryItemId}";
            StoredFileList fileList = restTemplateHelper.getForEntity(
                    StoredFileList.class, endpoint, "IDEA", idea.getId());
            if (fileList != null) {
                files = fileList.getStoredFiles();
            }

            action = String.format("/idea?user_id=%d", userId);
        } else {
            idea = new Idea();
            idea.setProperty(new Property());
            idea.getProperty().setId(propertyId);
            action = String.format("/idea?user_id=%d", userId);
        }

        model.addAttribute("files", files);
        model.addAttribute("category", StoredFile.FileCategory.IDEA.toString());
        model.addAttribute("action", action);
        model.addAttribute("userId", userId);
        model.addAttribute("idea", idea);
        return "/idea";
    }

    @PostMapping(value = "/idea", params = "action=save")
    public RedirectView saveIdea(Model model, HttpServletRequest request,
                                 @ModelAttribute("idea") Idea idea,
                                 @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (idea != null) {
            Idea savedIdea;
            String endpoint;

            if (idea.getId() == null || idea.getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/idea";
                savedIdea = restTemplateHelper.postForEntity(Idea.class, endpoint, idea,
                        idea.getProperty().getId());
            } else {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/idea/{ideaId}";
                savedIdea = restTemplateHelper.putForEntity(Idea.class, endpoint, idea,
                        idea.getProperty().getId(),
                        idea.getId());
            }

            message = "The idea was saved.";
            model.addAttribute("idea", savedIdea);
        } else {
            message = "The idea could not be saved.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/ideas?property_id=%d&user_id=%d", idea.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }

    @PostMapping(value = "/idea", params = "action=delete")
    public RedirectView deleteIdea(Model model, HttpServletRequest request,
                                   @ModelAttribute("idea") Idea idea,
                                   @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (idea != null) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/idea/{ideaId}";
            restTemplateHelper.delete(endpoint, idea.getProperty().getId(), idea.getId());

            message = "The idea was deleted.";
        } else {
            message = "The idea could not be deleted.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/ideas?property_id=%d&user_id=%d", idea.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }
}
