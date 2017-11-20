package com.danke.exception;

/**
 * 秒杀相关的所有业务异常
 * Created by xinxin on 2017/11/4.
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
