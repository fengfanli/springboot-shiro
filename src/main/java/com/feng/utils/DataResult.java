package com.feng.utils;

import lombok.Data;

/**
 * description: 与前端统一返回的数据 格式
 * author: 冯凡利
 * create:  2020/2/1 15:22
 */
@Data
public class DataResult<T> {

    /*
     * 请求响应code，0表示请求成功，其他表示失败
     * */
    private int code = 0;

    /*
     * 响应客户端的提示
     * */
    private String msg;

    /*
     * 响应客户端内容
     * */
    private T data;

    /*
    * 加上相应的构造方法  7个
    * */

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public DataResult(int code, T data) {
        this.code = code;
        this.msg = null;
        this.data = data;
    }

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T>DataResult getResult(int code, String msg){
        return new <T>DataResult(code, msg);
    }



}
