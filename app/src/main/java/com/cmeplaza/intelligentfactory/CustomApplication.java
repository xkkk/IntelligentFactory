package com.cmeplaza.intelligentfactory;

import android.app.Application;

import com.cme.corelib.CoreLib;
import com.cmeplaza.intelligentfactory.utils.AppConstant;

/**
 * Created by klx on 2018/5/10.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCoreLib();
    }

    private void initCoreLib() {
        CoreLib.init(this);
        CoreLib.initNet(AppConstant.BASE_URL, AppConstant.BASE_H5_URL);
    }
}
