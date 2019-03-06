package com.cme.corelib.http.retrofit;

/**
 * @author 徐坤
 * @time 16/8/19
 * @e-mail xukun1007@163.com
 */

public class HttpThrowable extends Throwable {



    private String code;
    private String msg;

    public HttpThrowable(String code , String message) {
        super(message);
        this.code = code;
        this.msg =message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
