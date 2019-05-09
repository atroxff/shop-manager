package com.goodrain.springbootdemo.service;

import com.goodrain.springbootdemo.dao.TagDao;
import com.goodrain.springbootdemo.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    /*获取所有分类*/
    public List<Tag> findAllTags() {
        return tagDao.findAllTags();
    }

    /*根据名称获取某个分类*/
    public Tag findTagByName(String cname) {
        return tagDao.findTagByName(cname);
    }
    /*添加分类*/
    public void addTag(Tag tag) {
        tagDao.addTag(tag);
    }
    /*修改分类*/
    public void updateTag(Tag tag) {
        tagDao.updateTag(tag);
    }
    /*删除分类*/
    public void deleteTag(String cid) {
        tagDao.deleteTag(cid);
    }
    /*删除商品分类表对应的分类*/
    public void deleteSockTag(String cid) {
        tagDao.deleteSockTag(cid);
    }
}
