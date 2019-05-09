package com.goodrain.springbootdemo.controller;

import com.goodrain.springbootdemo.constant.ConfigParam;
import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.entity.PageBean;
import com.goodrain.springbootdemo.entity.Tag;
import com.goodrain.springbootdemo.service.AdminService;
import com.goodrain.springbootdemo.util.DBConnPool;
import com.goodrain.springbootdemo.util.JSONObject;
import com.goodrain.springbootdemo.util.SysUtil;
import com.goodrain.springbootdemo.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ConfigParam configParam;
    @Autowired
    private AdminService adminService;

    @GetMapping("/dbdata")
    public @ResponseBody
    Object dbdata(Model model) {
//        DBConnPool instance = DBConnPool.getInstance();
//        try {
//            Connection conn = instance.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        String sql = "select * from sock";
        RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> items = jdbcTemplate.query(sql, rowMapper);
        return items;
    }

    /*首页*/
    @RequestMapping("/index.do")
    public String index() {
        return "model1/index";
    }

    /*登录页*/
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String toLogin() {
        return "model1/login";
    }

    /*登录*/
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, String username, String password, HttpSession session) {

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
            session.setAttribute("adminUser", true);
            return "model1/index";
        } else {
            model.addAttribute("msg", "请输入正确的用户名和密码");
            return "model1/login";
        }
    }

    /*退出登录*/
    @RequestMapping(value = "/logout.do")
    public String loginOut(HttpSession session) {
        session.removeAttribute("adminUser");
        return "model1/login";
    }

    /*商品管理*/
    @RequestMapping("/product.do")
    public String productManager(Model model, String pagenumStr, String pagesizeStr, String key, String type) {

        //参数校验
        if (StringUtils.isEmpty(pagenumStr)) {
            pagenumStr = "1";//默认第一页
        }
        if (StringUtils.isEmpty(pagesizeStr)) {
            pagesizeStr = configParam.getPagesize();//每页显示数量
        }
        int currentPage = Integer.parseInt(pagenumStr);
        int currentCount = Integer.parseInt(pagesizeStr);

        //List<Item> productList =adminService.findAllItems();
        PageBean pageBean = adminService.findItemsByPage(currentPage, currentCount);
        List<Item> productList = pageBean.getList();
        model.addAttribute("pagenumStr", pagenumStr);
        model.addAttribute("totalpage", pageBean.getTotalPage());
        model.addAttribute("count", pageBean.getTotalCount());
        model.addAttribute("productList", productList);
        return "model1/product_mng";
    }

    /*商品管理-跳转添加页*/
    @RequestMapping(value = "/product/add.do", method = RequestMethod.GET)
    public String toAddPage(Model model) {
        return "model1/product_add";
    }

    /*商品管理-添加商品*/
    @RequestMapping(value = "/product/add.do", method = RequestMethod.POST)
    public String addProduct(Model model, Item product, MultipartFile image_1, MultipartFile image_2) {
        //上传图片
        if (image_1 == null || image_2 == null) {
            return "model1/404";
        }

        //封装数据
        String sock_id = SysUtil.getUUID();
        product.setSock_id(sock_id);

        String path = "";
        try {
            File file = ResourceUtils.getFile("classpath:static"+File.separator+"item");
            if (!file.exists()) {
                file.mkdirs();
            }
            path=file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String newFileName1= image_1.getOriginalFilename();
        String newFileName2= image_2.getOriginalFilename();
        if(newFileName1.equals(newFileName2)){//同名文件+"_copy"
            newFileName2=newFileName1.substring(0,newFileName1.lastIndexOf("."))+"_copy"+newFileName1.substring(newFileName1.lastIndexOf("."));
        }
        File newFile1=new File(path+File.separator+newFileName1);
        File newFile2=new File(path+File.separator+newFileName2);
        //将内存数据写入磁盘,创建虚拟目录的图片
        try {
            image_1.transferTo(newFile1);
            image_2.transferTo(newFile2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

        String url1="item/"+newFileName1;
        String url2="item/"+newFileName2;
        //准备商品图片
        product.setImage_url_1(url1);
        product.setImage_url_2(url2);

        //调用service保存商品
        adminService.saveProduct(product);

        return "redirect:/admin/product.do";
    }

    /*商品管理-删除商品*/
    @RequestMapping(value = "/product/delete.do")
    public String deleteProduct(Model model, String id) {
        adminService.deleteProduct(id);
        return "redirect:/admin/product.do";
    }

    /*商品管理-跳转修改页*/
    @RequestMapping(value = "/product/update.do", method = RequestMethod.GET)
    public String toUpdatePage(Model model, String id) {

        Item product = adminService.findProductById(id);

        List<TagVo> tagList = adminService.findTagListByid(id);
        List<Tag> noTagList = adminService.findNoTagListByid(id);
        model.addAttribute("product", product);
        model.addAttribute("tagList", tagList);
        model.addAttribute("noTagList", noTagList);
        return "model1/product_chg";
    }

    /*商品管理-修改商品*/
    @RequestMapping(value = "/product/update.do", method = RequestMethod.POST)
    public String updateProduct(Model model, String sock_id, Item product, MultipartFile image_1, MultipartFile image_2, String pimage_pic1_text, String pimage_pic2_text) {


        //上传图片校验
        if (image_1 == null || image_2 == null) {
            return "model1/404";
        }

        //调用service保存商品
        adminService.deleteProduct(sock_id);

        String path = "";
        try {
            File file = ResourceUtils.getFile("classpath:static"+File.separator+"item");
            if (!file.exists()) {
                file.mkdirs();
            }
            path=file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String newFileName1= image_1.getOriginalFilename();
        String newFileName2= image_2.getOriginalFilename();
        if(newFileName1.equals(newFileName2)){//同名文件+"_copy"
            newFileName2=newFileName1.substring(0,newFileName1.lastIndexOf("."))+"_copy"+newFileName1.substring(newFileName1.lastIndexOf("."));
        }
        File newFile1=new File(path+File.separator+newFileName1);
        File newFile2=new File(path+File.separator+newFileName2);
        //将内存数据写入磁盘,创建虚拟目录的图片
        try {
            image_1.transferTo(newFile1);
            image_2.transferTo(newFile2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

        String url1="item/"+newFileName1;
        String url2="item/"+newFileName2;
        //准备商品图片
        product.setImage_url_1(url1);
        product.setImage_url_2(url2);

        //调用service保存商品
        adminService.saveProduct(product);

        return "redirect:/admin/product.do";
    }

    /*商品管理-删除标签*/
    @RequestMapping(value = "/product/update/deleteTag.do", method = RequestMethod.GET)
    public @ResponseBody Object deleteTag(Model model,String id,String tagid){
        JSONObject data = new JSONObject();
        //校验数据
        if (StringUtils.isEmpty(id)|| StringUtils.isEmpty(tagid)) {
            data.put("success", false);
            return data;
        }

        //删除商品的分类
        adminService.deleteProductTag(id,tagid);
        data.put("success", true);
        return data;
    }

    /*商品管理-添加标签*/
    @RequestMapping(value = "/product/update/addTag.do", method = RequestMethod.POST)
    public @ResponseBody Object addTag(Model model,String id,String tagid){
        JSONObject data = new JSONObject();
        //校验数据
        if (StringUtils.isEmpty(id)|| StringUtils.isEmpty(tagid)) {
            data.put("success", false);
            return data;
        }

       //添加商品的分类
       adminService.addProductTag(id,tagid);
        data.put("success", true);
        return data;
    }
}