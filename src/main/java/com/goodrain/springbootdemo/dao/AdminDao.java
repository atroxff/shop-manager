package com.goodrain.springbootdemo.dao;

import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.entity.Tag;
import com.goodrain.springbootdemo.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /*查询所有商品*/
    public List<Item> findAllItems() {
        String sql = "select * from sock";
        RowMapper<Item> rowMapper=new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> items = jdbcTemplate.query(sql, rowMapper);
        return items;
    }
    /*查询商品总数*/
    public int countAllItems() {
        String sql = "select count(*) from sock";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }
    /*分页查询商品*/
    public List<Item> findByPage(int index, int currentCount) {
        String sql = "select * from sock order by name asc limit ?,?";
        RowMapper<Item> rowMapper=new BeanPropertyRowMapper<Item>(Item.class);
        List<Integer> params = new ArrayList<>();
        params.add(index);
        params.add(currentCount);
        List<Item> items = jdbcTemplate.query(sql, rowMapper,index,currentCount);
        return items;
    }

    /*保存商品*/
    public void save(Item product) {
        String sql = "insert into sock value(?,?,?,?,?,?,?)";
        int row = jdbcTemplate.update(sql,
                product.getSock_id(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCount(),
                product.getImage_url_1(),
                product.getImage_url_2()
                );
    }

    /*删除商品*/
    public void delete(String id) {
        String sql = "delete from sock where sock_id=?";
        int row = jdbcTemplate.update(sql, id);

        String sql1 = "delete from sock_tag where sock_id=?";
        int row1 = jdbcTemplate.update(sql1, id);
    }

    /*根据id查找商品*/
    public Item findProductById(String id) {
        String sql = "select *  from sock where sock_id=?";
        RowMapper<Item> rowMapper=new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> itemList = jdbcTemplate.query(sql, rowMapper, id);
        if(itemList!=null&&itemList.size()!=0){
            return itemList.get(0);
        }
        return null;
    }

    /*根据id查找商品分类*/
    public List<TagVo> findTagListByid(String id) {
        String sql = "select *  from sock_tag,tag where sock_tag.tag_id=tag.tag_id and sock_tag.sock_id=?";
        RowMapper<TagVo> rowMapper=new BeanPropertyRowMapper<TagVo>(TagVo.class);
        List<TagVo> tagList = jdbcTemplate.query(sql, rowMapper, id);
        return tagList;
    }
    /*删除商品id*/
    public void deleteProductTag(String id, String tagid) {
        String sql = "delete from sock_tag where sock_id=? and tag_id=?";
        int row = jdbcTemplate.update(sql, id,tagid);
    }
    /*根据id查找商品没有的分类*/
    public List<Tag> findNoTagListByid(String id) {
        String sql = "select * from tag where tag_id not in"+
                "(select tag_id from sock_tag where sock_id=?)";

        RowMapper<Tag> rowMapper=new BeanPropertyRowMapper<Tag>(Tag.class);
        List<Tag> tagList = jdbcTemplate.query(sql, rowMapper, id);
        return tagList;
    }

    public void addProductTag(String id, String tagid) {
        String sql = "insert into sock_tag value(?,?)";
        int row = jdbcTemplate.update(sql, id,tagid);
    }
}
