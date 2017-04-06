package com.bibi.Baidu.Bean;

import java.io.Serializable;

/**
 * Created by bibinet on 2016/11/3.
 */
public class Result implements Serializable {
    private int flag;
    private String msg="",error="";
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    @Override
    public String toString() {
        return "Result [flag=" + flag + ", msg=" + msg + ", error=" + error
                + "]";
    }

}
