package com.clstephenson.homeinfo.web.controller;

import com.clstephenson.homeinfo.logging.MyLogger;
import com.clstephenson.homeinfo.model.*;
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
public class FeatureController {

    private final MyLogger LOGGER = new MyLogger(FeatureController.class);

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @GetMapping("/features")
    public String features(Model model, HttpServletRequest request,
                           @RequestParam(value = "user_id") long userId,
                           @RequestParam(value = "property_id") long propertyId) {
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/features";
        List<Feature> features = restTemplateHelper.getForEntity(FeatureList.class, endpoint, propertyId).getFeatures();

        model.addAttribute("userId", userId);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("features", features);
        return "features";
    }

    @GetMapping("/feature")
    public String feature(Model model, HttpServletRequest request,
                          @RequestParam(value = "feature_id", required = false) Long featureId,
                          @RequestParam(value = "property_id") Long propertyId,
                          @RequestParam(value = "user_id") Long userId) {
        Feature feature;
        List<StoredFile> files = Collections.emptyList();
        String action;
        if (featureId != null && featureId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/feature/{featureId}";
            feature = restTemplateHelper.getForEntity(Feature.class, endpoint, propertyId, featureId);
            userId = feature != null ? feature.getProperty().getUser().getId() : 0;

            // get any files associated with the feature
            endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/storedFiles/{category}/{categoryItemId}";
            StoredFileList fileList = restTemplateHelper.getForEntity(
                    StoredFileList.class, endpoint, "FEATURE", feature.getId());
            if (fileList != null) {
                files = fileList.getStoredFiles();
            }

            action = String.format("/feature?user_id=%d", userId);
        } else {
            feature = new Feature();
            feature.setProperty(new Property());
            feature.getProperty().setId(propertyId);
            feature.setLocation(new Location());
            action = String.format("/feature?user_id=%d", userId);
        }

        // get the list of locations
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/locations";
        List<Location> locations = restTemplateHelper.getForEntity(LocationList.class, endpoint, propertyId).getLocations();

        model.addAttribute("files", files);
        model.addAttribute("category", StoredFile.FileCategory.FEATURE.toString());
        model.addAttribute("locationList", locations);
        model.addAttribute("action", action);
        model.addAttribute("userId", userId);
        model.addAttribute("feature", feature);
        return "feature";
    }

    @PostMapping(value = "/feature", params = "action=save")
    public RedirectView saveFeature(Model model, HttpServletRequest request,
                                    @ModelAttribute("feature") Feature feature,
                                    @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (feature != null) {
            Feature savedFeature;
            String endpoint;

            if (feature.getLocation().getId() != null && feature.getLocation().getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/location/{locationId}";
                Location location = restTemplateHelper.getForEntity(Location.class, endpoint, feature.getLocation().getId());
                feature.setLocation(location);
            }

            if (feature.getId() == null || feature.getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/feature";
                savedFeature = restTemplateHelper.postForEntity(Feature.class, endpoint, feature,
                        feature.getProperty().getId());
            } else {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/feature/{featureId}";
                savedFeature = restTemplateHelper.putForEntity(Feature.class, endpoint, feature,
                        feature.getProperty().getId(),
                        feature.getId());
            }

            message = "The feature was saved.";
            model.addAttribute("feature", savedFeature);
        } else {
            message = "The feature could not be saved.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/features?property_id=%d&user_id=%d", feature.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }

    @PostMapping(value = "/feature", params = "action=delete")
    public RedirectView deleteFeature(Model model, HttpServletRequest request,
                                      @ModelAttribute("feature") Feature feature,
                                      @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (feature != null) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/feature/{featureId}";
            restTemplateHelper.delete(endpoint, feature.getProperty().getId(), feature.getId());

            message = "The feature was deleted.";
        } else {
            message = "The feature could not be deleted.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/features?property_id=%d&user_id=%d", feature.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }
}
