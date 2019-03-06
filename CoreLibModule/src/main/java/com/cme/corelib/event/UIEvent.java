package com.cme.corelib.event;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/4.
 * 页面间传递消息的事件类
 */

public class UIEvent {
    public static final String EVENT_PUSH_MSG = "event_push_msg"; // 传媒博客公告消息推送（自动跳转）
    public static final String EVENT_RE_LOGIN = "event_re_login"; // 重新登录

    public String event;
    private String message;

    private Bundle bundle = new Bundle();

    public UIEvent(String event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public UIEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void post() {
        EventBus.getDefault().post(this);
    }

    public void postSticky() {
        EventBus.getDefault().postSticky(this);
    }

    public UIEvent putExtra(String key, Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public UIEvent setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public String toString() {
        return "UIEvent{" +
                "event='" + event + '\'' +
                ", message='" + message + '\'' +
                ", bundle=" + bundle +
                '}';
    }
}
