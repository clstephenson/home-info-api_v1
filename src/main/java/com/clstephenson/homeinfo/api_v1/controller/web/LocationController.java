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
public class LocationController {

    private final MyLogger LOGGER = new MyLogger(LocationController.class);

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @GetMapping("/locations")
    public String locations(Model model, HttpServletRequest request,
                            @RequestParam(value = "user_id") long userId,
                            @RequestParam(value = "property_id") long propertyId) {
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/locations";
        List<Location> locations = restTemplateHelper.getForEntity(LocationList.class, endpoint, propertyId).getLocations();

        model.addAttribute("userId", userId);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("locations", locations);
        return "/locations";
    }

    @GetMapping("/location")
    public String location(Model model, HttpServletRequest request,
                           @RequestParam(value = "location_id", required = false) Long locationId,
                           @RequestParam(value = "property_id") Long propertyId,
                           @RequestParam(value = "user_id") Long userId) {
        Location location;
        List<StoredFile> files = Collections.emptyList();
        String action;
        if (locationId != null && locationId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/location/{locationId}";
            location = restTemplateHelper.getForEntity(Location.class, endpoint, propertyId, locationId);
            userId = location != null ? location.getProperty().getUser().getId() : 0;

            // get any files associated with the feature
            endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/storedFiles/{category}/{categoryItemId}";
            StoredFileList fileList = restTemplateHelper.getForEntity(
                    StoredFileList.class, endpoint, "LOCATION", location.getId());
            if (fileList != null) {
                files = fileList.getStoredFiles();
            }

            action = String.format("/location?user_id=%d", userId);
        } else {
            location = new Location();
            location.setProperty(new Property());
            location.getProperty().setId(propertyId);
            action = String.format("/location?user_id=%d", userId);
        }

        model.addAttribute("files", files);
        model.addAttribute("category", StoredFile.FileCategory.LOCATION.toString());
        model.addAttribute("action", action);
        model.addAttribute("userId", userId);
        model.addAttribute("location", location);
        return "/location";
    }

    @PostMapping(value = "/location", params = "action=save")
    public RedirectView saveLocation(Model model, HttpServletRequest request,
                                     @ModelAttribute("location") Location location,
                                     @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (location != null) {
            Location savedLocation;
            String endpoint;

            if (location.getId() == null || location.getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/location";
                savedLocation = restTemplateHelper.postForEntity(Location.class, endpoint, location,
                        location.getProperty().getId());
            } else {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/location/{locationId}";
                savedLocation = restTemplateHelper.putForEntity(Location.class, endpoint, location,
                        location.getProperty().getId(),
                        location.getId());
            }

            message = "The location was saved.";
            model.addAttribute("location", savedLocation);
        } else {
            message = "The location could not be saved.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/locations?property_id=%d&user_id=%d", location.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }

    @PostMapping(value = "/location", params = "action=delete")
    public RedirectView deleteLocation(Model model, HttpServletRequest request,
                                       @ModelAttribute("location") Location location,
                                       @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (location != null) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/location/{locationId}";
            restTemplateHelper.delete(endpoint, location.getProperty().getId(), location.getId());

            message = "The location was deleted.";
        } else {
            message = "The location could not be deleted.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/locations?property_id=%d&user_id=%d", location.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }
}
