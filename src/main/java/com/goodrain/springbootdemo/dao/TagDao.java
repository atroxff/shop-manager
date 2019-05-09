package com.goodrain.springbootdemo.dao;

import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Tag> findAllTags() {
        String sql = "select * from tag";
        RowMapper<Tag> rowMapper=new BeanPropertyRowMapper<Tag>(Tag.class);
        List<Tag> tags = jdbcTemplate.query(sql, rowMapper);
        return tags;
    }
    /*根据名称获取菜单*/
    public Tag findTagByName(String cname) {
        String sql = "select *  from tag where name=?";
        RowMapper<Tag> rowMapper=new BeanPropertyRowMapper<Tag>(Tag.class);
        List<Tag> tagList = jdbcTemplate.query(sql, rowMapper, cname);
        if(tagList!=null&&tagList.size()!=0){
            return tagList.get(0);
        }
        return null;
    }
    /*添加分类*/
    public void addTag(Tag tag) {
        String sql = "insert into tag value(?,?)";
        int row = jdbcTemplate.update(sql,
                tag.getTag_id(),
                tag.getName()
        );
    }
    /*修改分类*/
    public void updateTag(Tag tag) {
        String sql = "update tag set name = ? WHERE tag_id=?";
        int row = jdbcTemplate.update(sql,
                tag.getName(),
                tag.getTag_id()
        );
    }
    /*删除分类*/
    public void deleteTag(String cid) {
        String sql = "delete from tag where tag_id = ?";
        int row = jdbcTemplate.update(sql,cid);
    }
    /*删除分类在商品分类表的数据*/
    public void deleteSockTag(String cid) {
        String sql = "delete from sock_tag where tag_id = ?";
        int row = jdbcTemplate.update(sql,cid);
    }
}
