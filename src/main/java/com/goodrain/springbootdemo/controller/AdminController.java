package com.goodrain.springbootdemo.controller;

import com.goodrain.springbootdemo.constant.ConfigParam;
import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.entity.PageBean;
import com.goodrain.springbootdemo.service.AdminService;
import com.goodrain.springbootdemo.util.DBConnPool;
import com.goodrain.springbootdemo.util.SysUtil;
import org.springframework.beans.BeanUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ConfigParam configParam;
    @Autowired
    private AdminService adminService;

    @GetMapping("/dbdata")
    public @ResponseBody Object dbdata(Model model){
//        DBConnPool instance = DBConnPool.getInstance();
//        try {
//            Connection conn = instance.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        String sql = "select * from sock";
        RowMapper<Item> rowMapper=new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> items = jdbcTemplate.query(sql, rowMapper);
        return items;
    }
    /*首页*/
    @RequestMapping("/admin/index.do")
    public  String index(){
        return "model1/index";
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

    /*商品管理*/
    @RequestMapping("/admin/product.do")
    public  String productManager(Model model,String pagenumStr,String pagesizeStr,String key,String type){

        //参数校验
        if(StringUtils.isEmpty(pagenumStr)){
            pagenumStr="1";//默认第一页
        }
        if(StringUtils.isEmpty(pagesizeStr)){
            pagesizeStr=configParam.getPagesize();//每页显示数量
        }
        int currentPage = Integer.parseInt(pagenumStr);
        int currentCount = Integer.parseInt(pagesizeStr);

        //List<Item> productList =adminService.findAllItems();
        PageBean pageBean = adminService.findItemsByPage(currentPage,currentCount);

        model.addAttribute("pagenumStr", pagenumStr);
        model.addAttribute("totalpage", pageBean.getTotalPage());
        model.addAttribute("count", pageBean.getTotalCount());
        model.addAttribute("productList", pageBean.getList());
        return "model1/product_mng";
    }

    /*商品管理-跳转添加页*/
    @RequestMapping(value = "/admin/product/add.do",method = RequestMethod.GET)
    public  String toAddPage(Model model){
        return "model1/product_add";
    }

    /*商品管理-添加商品*/
    @RequestMapping(value = "/admin/product/add.do",method = RequestMethod.POST)
    public  String addProduct(Model model, Item product, MultipartFile image_1, MultipartFile image_2){
        //上传图片
        if(image_1 == null||image_2==null){
            return "model1/404";
        }

        //封装数据
        String sock_id = SysUtil.getUUID();
        product.setSock_id(sock_id);

        //准备商品图片
        product.setImage_url_1("item/phone1.jpg");
        product.setImage_url_2("item/phone1.jpg");

        //调用service保存商品
        adminService.saveProduct(product);

        return "redirect:/admin/product.do";
    }

    /*商品管理-删除商品*/
    @RequestMapping(value = "/admin/product/delete.do")
    public  String deleteProduct(Model model,String id){
        adminService.deleteProduct(id);
        return "redirect:/admin/product.do";
    }

    /*商品管理-跳转修改页*/
    @RequestMapping(value = "/admin/product/update.do",method = RequestMethod.GET)
    public  String toUpdatePage(Model model,String id){

        Item product = adminService.findProductById(id);
        model.addAttribute("product", product);
        return "model1/product_chg";
    }

    /*商品管理-修改商品*/
    @RequestMapping(value = "/admin/product/update.do",method = RequestMethod.POST)
    public  String updateProduct(Model model, String sock_id, Item product, MultipartFile image_1,MultipartFile image_2, String pimage_pic1_text,String pimage_pic2_text){

        product.setImage_url_1("item/image1.jpg");
        product.setImage_url_2("item/image1.jpg");
        //调用service保存商品
        adminService.deleteProduct(sock_id);
        adminService.saveProduct(product);

        return "redirect:/admin/product.do";
    }

}
