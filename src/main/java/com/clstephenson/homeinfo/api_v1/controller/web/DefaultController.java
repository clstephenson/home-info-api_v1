package com.clstephenson.homeinfo.api_v1.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DefaultController {

    @GetMapping("/")
    public RedirectView home() {
        //todo need to change this
        return new RedirectView("/properties?user_id=1");
    }

//    @GetMapping("/login")
//    public String login() {
//        return "/login";
//    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }


}
