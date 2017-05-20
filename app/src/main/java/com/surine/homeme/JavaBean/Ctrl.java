package com.surine.homeme.JavaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/5/19.
 */

public class Ctrl extends DataSupport {
    private int id;
    private int image_id;
    private String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setName(String name) {
        this.name = name;
    }


}
