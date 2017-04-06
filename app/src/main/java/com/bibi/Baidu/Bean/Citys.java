package com.bibi.Baidu.Bean;

import java.io.Serializable;

/**
 * Created by bibinet on 2016/10/31.
 */
public class Citys implements Serializable {
    private String name;

    public Citys(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Citys{" +
                "name='" + name + '\'' +
                '}';
    }
}
