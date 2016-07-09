package org.seckill.entity;

/**
 * Created by User on 2016/7/3.
 */
public class SeckillResult<T> {
    private boolean success;

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public SeckillResult(T data, boolean success) {

        this.data = data;
        this.success = success;
    }

    private T data;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
