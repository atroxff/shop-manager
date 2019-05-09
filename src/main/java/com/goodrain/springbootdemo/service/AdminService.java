package com.goodrain.springbootdemo.service;

import com.goodrain.springbootdemo.dao.AdminDao;
import com.goodrain.springbootdemo.entity.Item;
import com.goodrain.springbootdemo.entity.PageBean;
import com.goodrain.springbootdemo.entity.Tag;
import com.goodrain.springbootdemo.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    /*查找所有商品*/
    public List<Item> findAllItems() {
        return adminDao.findAllItems();
    }

    /*分页查找*/
    public PageBean findItemsByPage(int currentPage, int currentCount) {
        //封装一个PageBean 返回web层
        PageBean<Item> pageBean = new PageBean<Item>();

        //1、封装当前页
        pageBean.setCurrentPage(currentPage);
        //2、封装每页显示的条数
        pageBean.setCurrentCount(currentCount);
        //3、封装总条数
        int totalCount = adminDao.countAllItems() ;
        pageBean.setTotalCount(totalCount);
        //4、封装总页数
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);

        //5、当前页显示的数据
        // select * from product where cid=? limit ?,?
        // 当前页与起始索引index的关系
        int index = (currentPage-1)*currentCount;
        List<Item> list = adminDao.findByPage(index,currentCount);
        pageBean.setList(list);

        return pageBean;
    }

    /*添加商品*/
    public void saveProduct(Item product) {
        adminDao.save(product);
    }

    /*删除商品*/
    public void deleteProduct(String id) {
        adminDao.delete(id);
    }

    /*根据id查找商品*/
    public Item findProductById(String id) {
        return adminDao.findProductById(id);
    }

    /*根据id查找商品分类标签*/
    public List<TagVo> findTagListByid(String id) {
        return adminDao.findTagListByid(id);
    }

    /*删除商品标签*/
    public void deleteProductTag(String id, String tagid) {
        adminDao.deleteProductTag(id,tagid);
    }
    /*根据id查找商品没有的分类标签*/
    public List<Tag> findNoTagListByid(String id) {
        return adminDao.findNoTagListByid(id);
    }
    /*添加商品标签*/
    public void addProductTag(String id, String tagid) {
        adminDao.addProductTag(id,tagid);
    }
}
