package com.cme.corelib.mainmessage;

import android.os.Handler;
import android.os.Message;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by klx on 2018/3/20.
 * 消息的观察者
 */

public class MainQueueDataWatcher implements Observer {
    private Handler handler;

    public MainQueueDataWatcher(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void update(Observable o, Object data) {
        Message msg = Message.obtain();
        msg.obj = data;
        msg.setTarget(handler);
        handler.sendMessage(msg);
    }

    public Handler getHandler() {
        return handler;
    }
}
