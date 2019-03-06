package com.cme.corelib.utils.http;

import android.text.TextUtils;

import com.cme.corelib.utils.FileUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.StringUtils;
import com.cme.corelib.utils.http.download.DownloadProgressListener;

import java.io.File;

import rx.Subscriber;

/**
 * Created by klx on 2017/9/28.
 * 下载文件工具类
 */

public class DownLoadFileUtils {

    public static void downLoadFile(String url, String fileName, final DownloadListener downloadListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        final File outPutFile = new File(FileUtils.getCacheFile(true, true));
        downLoadFile(url, outPutFile.getAbsolutePath(), fileName, downloadListener);
    }

    public static void downLoadFile(String url, String filePath, String fileName, final DownloadListener downloadListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String baseUrl = StringUtils.getHostName(url);
        final File outPutFile = new File(filePath, fileName);
        new DownloadAPI(baseUrl, new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                if (downloadListener != null && contentLength > 0) {
                    downloadListener.onDownloading((int) (bytesRead * 100 / contentLength));
                }
            }
        })
                .downloadAPK(url, outPutFile, new Subscriber() {
                    @Override
                    public void onCompleted() {
                        if (downloadListener != null) {
                            downloadListener.onDownloadSuccess(outPutFile);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (downloadListener != null) {
                            downloadListener.onDownLoadFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    /**
     * 下载回调监听
     */
    public interface DownloadListener {
        void onDownloadSuccess(File file);

        void onDownloading(int progress);

        void onDownLoadFail(String reason);
    }
}
