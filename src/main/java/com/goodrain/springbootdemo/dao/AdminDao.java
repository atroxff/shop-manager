package com.goodrain.springbootdemo.dao;

import com.goodrain.springbootdemo.entity.Item;
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
    }

    /*根据id查找商品*/
    public Item findProductById(String id) {
        String sql = "select *  from sock where sock_id=?";
        RowMapper<Item> rowMapper=new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> itemList = jdbcTemplate.query(sql, rowMapper, id);
        if(itemList!=null){
            return itemList.get(0);
        }
        return null;
    }


}
