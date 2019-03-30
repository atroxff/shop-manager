package com.goodrain.springbootdemo.controller;

import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.util.DBConnPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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

    /*登录页*/
    @RequestMapping(value="/admin/login.do",method = RequestMethod.GET)
    public  String toLogin(){
        return "model1/login";
    }

    /*登录*/
    @RequestMapping(value="/admin/login",method = RequestMethod.POST)
    public  String login(Model model, String username, String password, HttpSession session){

        //数据回显
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        //校验非空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            model.addAttribute("msg", "请输入用户名和密码");
            return "model1/login";
        }
        //用户认证
        if (username.equals("admin") && password.equals("Abc123++")) {
            session.setAttribute("adminUser",true);
            return "model1/index";
        }else{
            model.addAttribute("msg", "请输入正确的用户名和密码");
            return "model1/login";
        }
    }

    /*退出登录*/
    @RequestMapping(value="/admin/logout.do")
    public  String loginOut(HttpSession session){
        session.removeAttribute("adminUser");
        return "model1/login";
    }
}
