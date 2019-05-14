package com.goodrain.springbootdemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {

        return "model1/login";

    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/test")
    public @ResponseBody Object test() {
        String path = "src";
        try {
            File file = ResourceUtils.getFile("classpath:static"+File.separator+"item");
            //ResourceUtils.getURL();
            //path=file.getPath();
            path=ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

       return path;
    }

}
