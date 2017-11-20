package com.danke.dto;

/**
 * 将所有的ajax请求返回类型，全部封装成json数据
 * Created by xinxin on 2017/11/4.
 */
public class SeckillResult<T> {
    private boolean success;
    private String error;
    private T data;

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
