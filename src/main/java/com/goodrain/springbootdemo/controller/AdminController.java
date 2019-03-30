package com.goodrain.springbootdemo.controller;

import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.util.DBConnPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/dbdata")

    public @ResponseBody Object dbdata(Model model){
        DBConnPool instance = DBConnPool.getInstance();
        try {
            Connection conn = instance.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "select * from sock";
        RowMapper<Item> rowMapper=new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> items = jdbcTemplate.query(sql, rowMapper);
        return items;
    }
}
