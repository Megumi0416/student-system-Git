package com.example.entity;

// 学院
public class College {

    private Integer id;     //主键ID
    private String name;    //学院名称
    private String dean;  //院长

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean;
    }
}
