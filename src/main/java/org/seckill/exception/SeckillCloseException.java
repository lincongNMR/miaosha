package org.seckill.exception;

/**
 * Created by linyitian on 2016/6/22.
 */
public class SeckillCloseException extends RuntimeException{
    public SeckillCloseException(String message,Throwable cause) {
        super(message,cause);
    }
    public SeckillCloseException(String message) {
        super(message);
    }

}
