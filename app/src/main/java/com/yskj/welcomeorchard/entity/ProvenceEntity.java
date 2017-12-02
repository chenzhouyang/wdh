package com.yskj.welcomeorchard.entity;

import java.util.List;

/**
 * Created by YSKJ-JH on 2017/3/13.
 */

public class ProvenceEntity {
    private String id;
    private String name;
    private List<CityEntity> citys;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<CityEntity> getCitys() {
        return citys;
    }
    public void setCitys(List<CityEntity> citys) {
        this.citys = citys;
    }
    @Override
    public String toString() {
        return name;
    }


}
