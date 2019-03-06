package com.cme.corelib.bean;

/**
 * Created by klx on 2017/6/6.
 */

public class BaseModule<T> {

    /**
     * state : 1
     * message : 请求成功
     * data : null
     * code : 50000
     */

    private int state;
    private String message;
    private T data;
    private String code;
    private ErrorBean error;

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class ErrorBean {
        /**
         * msg : 验证码错误！
         * code : 1005
         */

        private String msg;
        private String code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ErrorBean{" +
                    "msg='" + msg + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }

    public boolean isSuccess() {
        return 1 == state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseModule{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code='" + code + '\'' +
                '}';
    }
}
