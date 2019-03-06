package com.cme.corelib.http.exception;

/**
 * Created by klx on 2017/5/15.
 */

public class NoNetException extends RuntimeException{
    public NoNetException() {
        super("没有网络，请设置");
    }
}
