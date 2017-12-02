package com.yskj.welcomeorchard.entity;

/**
 * Created by YSKJ-JH on 2017/1/12.
 */

public class GoodCateTypeEntity {
    private int id;
    private String typename;
    private String typeiconurl;


    public GoodCateTypeEntity(int id, String typename, String typeiconurl)
    {
        super();
        this.id=id;
        this.typename=typename;
        this.typeiconurl=typeiconurl;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTypename() {
        return typename;
    }
    public void setTypename(String typename) {
        this.typename = typename;
    }
    public String getTypeiconurl() {
        return typeiconurl;
    }
    public void setTypeiconurl(String typeiconurl) {
        this.typeiconurl = typeiconurl;
    }


}
