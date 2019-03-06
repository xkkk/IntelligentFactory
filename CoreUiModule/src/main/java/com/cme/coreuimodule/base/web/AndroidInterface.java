package com.cme.coreuimodule.base.web;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.cme.corelib.CoreLib;
import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.secret.Sha1;
import com.cme.corelib.utils.CommonUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.bigimage.DownLoadFilePresenter;
import com.cme.coreuimodule.base.utils.CommonDialogUtils;
import com.common.coreuimodule.R;
import com.just.agentweb.AgentWeb;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Created by xiaozi on 2018/3/13.
 */

public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Activity context;

    public AndroidInterface(AgentWeb agent, Activity context) {
        this.agent = agent;
        this.context = context;
    }


    /**
     * h5页面返回
     */
    @JavascriptInterface
    public void back() {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                context.finish();
            }
        });
    }
    /**
     * h5页面返回,带有返回参数
     */
    @JavascriptInterface
    public void back(Object message) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                context.finish();
            }
        });
    }

    /**
     * h5页面返回
     */
    @JavascriptInterface
    public void goBack() {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                context.finish();
            }
        });
    }
    /**
     * web 点击唤醒本地应用链接
     *
     * @param wakeUpLink
     * @param APPName
     */
    @JavascriptInterface
    public void wakeUpAPP(final String wakeUpLink, String APPName) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                CommonUtils.openOtherAppByPackageName(context, wakeUpLink);
            }
        });
    }

    /**
     * 通过包名打开App
     */
    @JavascriptInterface
    public void openIntelligentManApp(final String packageName) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                CommonUtils.openOtherAppByPackageName(context, packageName);
            }
        });

    }

    /**
     * 显示弹窗
     */
    @JavascriptInterface
    public void alertMessage(final String message) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                CommonDialogUtils.showOnlyConfirmDialog(context, message, null);
            }
        });
    }

    /**
     * 打开本地浏览器
     */
    @JavascriptInterface
    public void pushLinkVC(final String link) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.i(link);
                SimpleWebActivity.startActivity(context, link, "登录");
            }
        });
    }

    /**
     * 复制文本
     */
    @JavascriptInterface
    public void copy(final String content) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                UiUtil.copy(CoreLib.getContext(), content);
            }
        });

    }

    /**
     * 打开手机默认浏览器
     *
     * @param url
     */
    @JavascriptInterface
    public boolean openDefaultBrowser(String url) {
        return CommonUtils.openWeb(context, url);
    }

    /**
     * 下载图片到本地
     *
     * @param imageUrl
     */
    @JavascriptInterface
    public void downloadImageToLocal(final String imageUrl) {
        // 请求权限
        new RxPermissions(context).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            new DownLoadFilePresenter().downLoadImage(imageUrl, Sha1.shaEncrypt(imageUrl).substring(0, 10) + ".jpg");
                        } else {
                            CommonDialogUtils.showSetPermissionDialog(context, context.getString(R.string.downloadPermissionTip),
                                    null, null);
                        }
                    }
                });
    }

    /**
     * 打开手机相册
     *
     * @param imageUrl
     */
    @JavascriptInterface
    public void openImage(final String imageUrl) {
        // 请求权限
        new RxPermissions(context).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            new DownLoadFilePresenter().downLoadImage(imageUrl, Sha1.shaEncrypt(imageUrl).substring(0, 10) + ".jpg");
                        } else {
                            CommonDialogUtils.showSetPermissionDialog(context, context.getString(R.string.downloadPermissionTip),
                                    null, null);
                        }
                    }
                });
    }

    /**
     * 打开相机
     *
     * @param imageUrl
     */
    @JavascriptInterface
    public void openCamera(final String imageUrl) {
        // 请求权限
        new RxPermissions(context).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            new DownLoadFilePresenter().downLoadImage(imageUrl, Sha1.shaEncrypt(imageUrl).substring(0, 10) + ".jpg");
                        } else {
                            CommonDialogUtils.showSetPermissionDialog(context, context.getString(R.string.downloadPermissionTip),
                                    null, null);
                        }
                    }
                });
    }



    @JavascriptInterface
    public void showHtmlCode(String html){
        LogUtils.xml("cme","网页源码： "+html);
    }

}
