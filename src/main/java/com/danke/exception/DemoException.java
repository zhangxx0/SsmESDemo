package com.danke.exception;

/**
 * 秒杀相关的所有业务异常
 * Created by xinxin on 2017/11/4.
 */
public class DemoException extends RuntimeException{

    public DemoException(String message) {
        super(message);
    }

    public DemoException(String message, Throwable cause) {
        super(message, cause);
    }
}
