package com.clstephenson.homeinfo.api_v1.controller.web;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.model.VendorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class VendorController {

    private final MyLogger LOGGER = new MyLogger(VendorController.class);

    @Autowired
    private RestTemplateHelper restTemplateHelper;

    @GetMapping("/vendors")
    public String vendors(Model model, HttpServletRequest request,
                          @RequestParam(value = "user_id") long userId) {
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/user/{userId}/vendors";
        List<Vendor> vendors = restTemplateHelper.getForEntity(VendorList.class, endpoint, userId).getVendors();

        model.addAttribute("userId", userId);
        model.addAttribute("vendors", vendors);
        return "vendors";
    }

    @GetMapping("/vendor")
    public String vendor(Model model, HttpServletRequest request,
                         @RequestParam(value = "vendor_id", required = false) Long vendorId,
                         @RequestParam(value = "user_id") Long userId) {
        Vendor vendor;
        String action;
        if (vendorId != null && vendorId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/vendor/{vendorId}";
            vendor = restTemplateHelper.getForEntity(Vendor.class, endpoint, vendorId);
            userId = vendor != null ? vendor.getUser().getId() : 0;
            action = String.format("/vendor?vendor_id=%d&user_id=%d", vendor.getId(), userId);
        } else {
            vendor = new Vendor();
            action = String.format("/vendor?user_id=%d", userId);
        }

        model.addAttribute("action", action);
        model.addAttribute("userId", userId);
        model.addAttribute("vendor", vendor);
        return "vendor";
    }

    @PostMapping(value = "/vendor", params = "action=save")
    public RedirectView saveVendor(Model model, HttpServletRequest request,
                                   @ModelAttribute("vendor") Vendor vendor,
                                   @RequestParam(value = "user_id") long userId) {
        String message = "";
        if (vendor != null) {
            Vendor savedVendor;
            String endpoint;
            if (vendor.getId() == null || vendor.getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/user/{userId}/vendor";
                savedVendor = restTemplateHelper.postForEntity(Vendor.class, endpoint, vendor, userId);
            } else {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/vendor/{vendorId}";
                savedVendor = restTemplateHelper.putForEntity(Vendor.class, endpoint, vendor, vendor.getId());
            }

            message = "The vendor was saved.";
            model.addAttribute("vendor", savedVendor);
        } else {
            message = "The vendor could not be saved.  Please try again.";
        }
        model.addAttribute("message", message);
        return new RedirectView("/vendors?user_id=" + userId);
    }

    @PostMapping(value = "/vendor", params = "action=delete")
    public RedirectView deleteVendor(Model model, HttpServletRequest request,
                                     @ModelAttribute("vendor") Vendor vendor,
                                     @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (vendor != null) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/vendor/{vendorId}";
            restTemplateHelper.delete(endpoint, vendor.getId());

            message = "The vendor was deleted.";
        } else {
            message = "The vendor could not be deleted.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/vendors?user_id=%d", userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }
}
