package com.goodrain.springbootdemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class ErrorController {

    @RequestMapping("/401")
    public String toPage401() {
        return "model/404";
    }
    @RequestMapping("/404")
    public String toPage404() {
        return "model/404";
    }

    @RequestMapping("/405")
    public String toPage400() {
        return "model/404";
    }

    @RequestMapping("/500")
    public String toPage500() {
        return "model/404";
    }
}