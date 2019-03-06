package com.cme.coreuimodule.base.bigimage;


import com.cme.coreuimodule.base.mvp.BaseContract;

/**
 * Created by klx on 2017/6/26.
 */

public interface DownLoadFileContract {
    interface IDownLoadFileView extends BaseContract.BaseView {
        void onDownLoadResult(boolean result, String path);
        void downLoadProgress(float progress, long total);
    }

    interface IDownLoadFilePresenter {
        void downLoadFile(String path, String fileName);
        void downLoadImage(String path, String fileName);
    }
}
