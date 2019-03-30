package com.goodrain.springbootdemo.entity;



//@Entity
//@Table(name = "sock")
public class Item {
   // @Id
    //@Column(name = "sock_id")
    private String sock_id;
   // @Column
    private String name;
    //@Column
    private String description;
    //@Column
    private double price;
    //@Column
    private int count;
    //@Column(name = "image_url_1")
    private String image_url_1;
    //@Column(name = "image_url_2")
    private String image_url_2;

    public String getSock_id() {
        return sock_id;
    }

    public void setSock_id(String sock_id) {
        this.sock_id = sock_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImage_url_1() {
        return image_url_1;
    }

    public void setImage_url_1(String image_url_1) {
        this.image_url_1 = image_url_1;
    }

    public String getImage_url_2() {
        return image_url_2;
    }

    public void setImage_url_2(String image_url_2) {
        this.image_url_2 = image_url_2;
    }
}
