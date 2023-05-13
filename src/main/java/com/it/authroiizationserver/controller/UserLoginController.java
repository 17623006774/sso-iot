package com.it.authroiizationserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserLoginController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @RequestMapping("/home")
    public String home(){
        return "redirect:http://localhost:8082/view";
    }
}
