package com.clstephenson.homeinfo.api_v1.controller.web;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.PropertyList;
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
public class PropertyController {

    private final MyLogger LOGGER = new MyLogger(PropertyController.class);

    @Autowired
    private RestTemplateHelper restTemplateHelper;

    @GetMapping("/properties")
    public String properties(Model model, HttpServletRequest request,
                             @RequestParam(value = "user_id") long userId) {
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/properties/user/{userId}";
        PropertyList propertyList = restTemplateHelper.getForEntity(PropertyList.class, endpoint, userId);
        List<Property> properties = propertyList == null ? Collections.emptyList() : propertyList.getProperties();

        model.addAttribute("userId", userId);
        model.addAttribute("properties", properties);
        return "/properties";
    }

    @GetMapping("/property")
    public String property(Model model, HttpServletRequest request,
                           @RequestParam(value = "property_id", required = false) Long propertyId,
                           @RequestParam(value = "user_id") Long userId) {
        Property property;
        String action;
        if (propertyId != null && propertyId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}";
            property = restTemplateHelper.getForEntity(Property.class, endpoint, propertyId);
            userId = property != null ? property.getUser().getId() : 0;
            action = String.format("/property?property_id=%d&user_id=%d", property.getId(), userId);
        } else {
            property = new Property();
            action = String.format("/property?user_id=%d", userId);
        }

        model.addAttribute("action", action);
        model.addAttribute("userId", userId);
        model.addAttribute("property", property);
        return "/property";
    }

    @PostMapping(value = "/property", params = "action=save")
    public String saveProperty(Model model, HttpServletRequest request,
                               @ModelAttribute("property") Property property,
                               @RequestParam(value = "user_id") long userId) {
        String message = "";
        if (property != null) {
            Property savedProperty;
            String endpoint;
            if (property.getId() == null || property.getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/user/{userId}";
                savedProperty = restTemplateHelper.postForEntity(Property.class, endpoint, property, userId);
            } else {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}";
                savedProperty = restTemplateHelper.putForEntity(Property.class, endpoint, property, property.getId());
            }

            message = "The property was saved.";
            model.addAttribute("property", savedProperty);
        } else {
            message = "The property could not be saved.  Please try again.";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("message", message);
        return "/property";
    }

    @PostMapping(value = "/property", params = "action=delete")
    public RedirectView deleteVendor(Model model, HttpServletRequest request,
                                     @ModelAttribute("property") Property property,
                                     @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (property != null) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}";
            restTemplateHelper.delete(endpoint, property.getId());

            message = "The property was deleted.";
        } else {
            message = "The property could not be deleted.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/properties?user_id=%d", userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }
}
