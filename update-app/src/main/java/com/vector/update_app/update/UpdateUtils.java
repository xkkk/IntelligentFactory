package com.vector.update_app.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.cme.corelib.CoreLib;
import com.cme.corelib.utils.CommonUtils;
import com.cme.corelib.utils.FileUtils;
import com.cme.corelib.utils.GsonUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.NetworkUtils;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;

import java.io.File;

/**
 * Created by klx on 2017/12/11.
 * app更新工具类
 */

public class UpdateUtils {
    public void checkUpdate(final Activity activity, final boolean isShowTip) {
//        final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String path = FileUtils.getCacheFile(true, true);
        LogUtils.i("下载路径：" + path);
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(activity)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(CoreLib.getBaseUrl() + "/api")
                //以下设置，都是可选
                //设置apk下砸路径，默认是在下载到sd卡下/download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        VersionBean versionBean = GsonUtils.parseJsonWithGson(json, VersionBean.class);
                        if (versionBean != null && versionBean.getData() != null) {
                            final VersionBean.DataBean data = versionBean.getData();
                            int latestVersionCode = data.getVersionCode();
                            int localVersionCode = Integer.parseInt(CommonUtils.getVersionCode());
                            if (latestVersionCode > localVersionCode) {
                                updateAppBean.setUpdate("Yes")
                                        .setNewMd5(data.getMd5())
                                        .setNewVersion(data.getVersionName())
                                        .setApkFileUrl(data.getUrl())
                                        .setUpdateLog(data.getUpdateInfo())
                                        .setTargetSize(data.getApkSize())
                                        .setConstraint(data.getIsForceUpdate())
                                        .setTargetPath(path);
                            }
                        }
                        return updateAppBean;
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    @Override
                    public void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        if (!TextUtils.isEmpty(updateApp.getApkFileUrl())) {
                            if (AppUpdateUtils.appIsDownloaded(updateApp)) {
                                //自定义对话框
                                showDiyDialog(activity, updateApp, updateAppManager, true, true);
                            } else {
                                //自定义对话框
                                if (NetworkUtils.isWifiConnected(activity)) {
                                    if (isShowTip) {
                                        showDiyDialog(activity, updateApp, updateAppManager, true, false);
                                    } else {
                                        // wifi连接，自动下载
                                        updateApp.dismissNotificationProgress(true);
                                        updateAppManager.download();
                                    }
                                } else {
                                    showDiyDialog(activity, updateApp, updateAppManager, true, false);
                                }
                            }
                        }
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
//                        CProgressDialogUtils.cancelProgressDialog(activity);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String message) {
                        if (isShowTip) {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
//                        if (isShowTip) {
//                            CProgressDialogUtils.showProgressDialog(activity);
//                        }
                    }
                });
    }

    /**
     * 自定义对话框
     *
     * @param updateApp
     * @param updateAppManager
     */
    private void showDiyDialog(final Activity activity, final UpdateAppBean updateApp, final UpdateAppManager updateAppManager,
                               final boolean isShowDownloadProgress, final boolean hasDownload) {
//        String targetSize = updateApp.getTargetSize();
        String updateLog = updateApp.getUpdateLog();

        String msg = "";

//        if (!TextCommon.isEmpty(targetSize)) {
//            msg = "新版本大小：" + targetSize + "\n\n";
//        }

        String cancelText = "暂不升级";
        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog;
            cancelText = "";
        }

        new AlertDialog.Builder(activity)
                .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                .setMessage(msg)
                .setCancelable(!updateApp.isConstraint())
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hasDownload) {
                            AppUpdateUtils.installApp(activity, AppUpdateUtils.getAppFile(updateApp));
                        } else {
                            //显示下载进度
                            if (isShowDownloadProgress) {
                                updateAppManager.download(new DownloadService.DownloadCallback() {
                                    @Override
                                    public void onStart() {
                                        HProgressDialogUtils.showHorizontalProgressDialog(activity, "下载进度", false);
                                    }

                                    /**
                                     * 进度
                                     *
                                     * @param progress  进度 0.00 -1.00 ，总大小
                                     * @param totalSize 总大小 单位B
                                     */
                                    @Override
                                    public void onProgress(float progress, long totalSize) {
                                        HProgressDialogUtils.setProgress(Math.round(progress * 100));
                                    }

                                    /**
                                     *
                                     * @param total 总大小 单位B
                                     */
                                    @Override
                                    public void setMax(long total) {

                                    }


                                    @Override
                                    public boolean onFinish(File file) {
                                        HProgressDialogUtils.cancel();
                                        return true;
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                                        HProgressDialogUtils.cancel();

                                    }
                                });
                            } else {
                                //不显示下载进度
                                updateAppManager.download();
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
