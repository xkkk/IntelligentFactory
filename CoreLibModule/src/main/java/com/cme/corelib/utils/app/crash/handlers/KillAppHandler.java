package com.cme.corelib.utils.app.crash.handlers;


import com.cme.corelib.utils.app.crash.IHandlerException;

/**
 * Created by zhangzheng on 2017/4/5.
 */

public class KillAppHandler implements IHandlerException {
    @Override
    public boolean handler(Throwable e) {
        android.os.Process.killProcess(android.os.Process.myPid());
        return true;
    }
}
