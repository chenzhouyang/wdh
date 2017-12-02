package com.yskj.welcomeorchard.entity;

import java.util.ArrayList;

/**
 * 城市列表实体类
 */
public class CityBean {

    private int id;
    private String name;
    private ArrayList<CityBean> city = new ArrayList<CityBean>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CityBean> getCity() {
        return city;
    }

    public void setCity(ArrayList<CityBean> city) {
        this.city = city;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CityBean{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city=").append(city);
        sb.append('}');
        return sb.toString();
    }
}
