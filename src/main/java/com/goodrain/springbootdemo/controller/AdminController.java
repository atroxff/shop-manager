package com.goodrain.springbootdemo.controller;

import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.repositoriy.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/dbdata")

    public @ResponseBody Object dbdata(Model model){
        List<Item> itemList = itemRepository.findAll();
        model.addAttribute("itemList", itemList);
        return itemList;
    }
}
