package com.clstephenson.homeinfo.api_v1.controller.web;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

public class ControllerHelper {

    public static String getUrlBase(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
    }

    public static RedirectView redirectTo(String source, String destination, MyLogger logger) {
        logger.info(String.format("Redirecting from '%s' to '%s'", source, destination));
        return new RedirectView(destination);
    }
}
