package com.cme.coreuimodule.base.bigimage;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.cme.corelib.CoreLib;
import com.cme.corelib.utils.UiUtil;
import com.cme.corelib.utils.http.DownLoadFileUtils;
import com.cme.coreuimodule.base.mvp.RxPresenter;

import java.io.File;

/**
 * Created by klx on 2017/6/26.
 * 下载文件的
 */

public class DownLoadFilePresenter extends RxPresenter<DownLoadFileContract.IDownLoadFileView> implements DownLoadFileContract.IDownLoadFilePresenter {
    @Override
    public void downLoadFile(String path, String fileName) {
        DownLoadFileUtils.downLoadFile(path, fileName, new DownLoadFileUtils.DownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                mView.onDownLoadResult(true, file.getAbsolutePath());
            }

            @Override
            public void onDownloading(int progress) {
                mView.downLoadProgress(progress, 100);
            }

            @Override
            public void onDownLoadFail(String reason) {
                mView.onDownLoadResult(false, "");
            }
        });
    }

    @Override
    public void downLoadImage(String path, String fileName) {
        File cacheFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        File file = new File(cacheFilePath, fileName);
        if (file.exists() && file.isFile()) {
            if (mView != null) {
                mView.onDownLoadResult(true, file.getAbsolutePath());
            } else {
                UiUtil.showToast("图片已保存至" + path);
            }
            return;
        }
        DownLoadFileUtils.downLoadFile(path, cacheFilePath.getAbsolutePath(), fileName, new DownLoadFileUtils.DownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (mView != null) {
                    mView.onDownLoadResult(true, file.getAbsolutePath());
                } else {
                    UiUtil.showToast("图片已保存至" + file.getAbsolutePath());
                }
                // 最后通知图库更新
                CoreLib.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)));
            }

            @Override
            public void onDownloading(int progress) {
                if (mView != null) {
                    mView.downLoadProgress(progress, 100);
                }
            }

            @Override
            public void onDownLoadFail(String reason) {
                if (mView != null) {
                    mView.onDownLoadResult(false, "");
                } else {
                    UiUtil.showToast("下载失败，请重试");
                }
            }
        });
    }
}
