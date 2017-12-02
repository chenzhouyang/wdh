package com.yskj.welcomeorchard.entity;

import java.util.List;

/**
 * Created by YSKJ-JH on 2017/3/13.
 */

public class CityEntity {
    private String id;
    private String name;
    private List<DistrictEntity> districts;
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


    public List<DistrictEntity> getDistricts() {
        return districts;
    }
    public void setDistricts(List<DistrictEntity> districts) {
        this.districts = districts;
    }
    @Override
    public String toString() {
        return name;
    }

}
