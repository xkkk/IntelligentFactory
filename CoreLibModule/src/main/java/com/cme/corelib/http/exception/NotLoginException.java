package com.cme.corelib.http.exception;

/**
 * Created by klx on 2017/5/15.
 */

public class NotLoginException extends RuntimeException{

    public NotLoginException(String message) {
        super(message);
    }

    public NotLoginException() {
        super("请重新请求");
    }
}
