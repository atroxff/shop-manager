package com.goodrain.springbootdemo.controller;

import com.goodrain.springbootdemo.entity.Tag;
import com.goodrain.springbootdemo.service.TagService;
import com.goodrain.springbootdemo.util.JSONObject;
import com.goodrain.springbootdemo.util.SysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang3.StringUtils;


import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    TagService tagService;
    /*分类管理*/
    @RequestMapping("/tag.do")
    public String tagManager(Model model, String pagenumStr, String pagesizeStr, String key, String type) {
        List<Tag> tagList = tagService.findAllTags();
        model.addAttribute("tagList",tagList);
        return "model1/category_mng";
    }

    /*分类管理-添加*/
    @RequestMapping(value="/tag/add.do",method = RequestMethod.POST)
    public  @ResponseBody
    Object categoryAdd(Model model, String cname){
        JSONObject data = new JSONObject();

        //校验数据
        if (StringUtils.isEmpty(cname) ) {
            data.put("success", false);
            return data;
        }

        //封装菜单类
        Tag tag =new Tag();
        List<Tag> tagList = tagService.findAllTags();//得到最后一个菜单的cid迭代
        String tag_id=null;
        if(tagList.size()!=0){
            tag_id = tagList.get(tagList.size()-1).getTag_id();
        }else{
            tag_id = "1";
        }
        tag_id = SysUtil.strNumAdd(tag_id);
        tag.setTag_id(tag_id);
        tag.setName(cname);
        //调用service添加菜单
        Tag result = tagService.findTagByName(cname);

        if(result == null) {
            tagService.addTag(tag);
            data.put("success", true);
        }else{
            data.put("success", false);
            data.put("msg", "分类名称已存在！");
        }
        return data;
    }

    /*分类管理-修改*/
    @RequestMapping(value="/tag/update.do")
    public  @ResponseBody Object categoryChange(Model model,String cid,String cname){
        JSONObject data = new JSONObject();

        //校验数据
        if (StringUtils.isEmpty(cid)||StringUtils.isEmpty(cname)) {
            data.put("success", false);
            return data;
        }
        //封装
        Tag tag =new Tag();
        tag.setTag_id(cid);
        tag.setName(cname);
        //调用service修改菜单
        Tag result = tagService.findTagByName(cname);

        if(result == null) {
            tagService.updateTag(tag);
            data.put("success", true);
        }else{
            if(!result.getTag_id().equals(tag.getTag_id())){//修改后有重名
                data.put("success", false);
                data.put("msg", "分类名称已存在！编号为"+result.getTag_id());
            }else{//不修改
                data.put("success", true);
            }
        }

        return data;
    }

    /*分类管理-删除*/
    @RequestMapping(value="/tag/delete.do")
    public  String categoryRemove(Model model, String cid){

        //校验数据
        if(StringUtils.isEmpty(cid)){
            model.addAttribute("msg", "系统异常：请求参数cid为空");
            return "model1/404.html";
        }

        //调用service删除分类,同时删除tag表和sock_tag表
        tagService.deleteTag(cid);
        tagService.deleteSockTag(cid);

        return "redirect:/admin/tag.do";
    }
}
