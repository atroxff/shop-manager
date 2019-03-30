package com.goodrain.springbootdemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {

        return "index";

    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
