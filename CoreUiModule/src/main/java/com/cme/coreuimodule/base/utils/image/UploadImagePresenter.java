package com.cme.coreuimodule.base.utils.image;

import com.cme.corelib.http.CommonHttpUtils;
import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.GsonUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.image.ImageBean;
import com.cme.coreuimodule.base.mvp.RxPresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by klx on 2017/12/25.
 * 上传图片Presenter
 */

public class UploadImagePresenter extends RxPresenter<UploadImageContract.IUploadImageView> implements UploadImageContract.IUploadImagePresenter {
    @Override
    public void uploadImage(String filePath) {
        uploadImage(filePath, System.currentTimeMillis() + ".jpg");
    }

    public void uploadImage(String filePath, String fileName) {
        CommonHttpUtils.uploadFile(filePath, fileName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscribe<String>() {
                    @Override
                    public void onNext(String s) {
                        ImageBean imageBean = GsonUtils.parseJsonWithGson(s, ImageBean.class);
                        if (imageBean != null && imageBean.isSuccess()) {
                            mView.onUploadImage(imageBean);
                        } else {
                            mView.onUploadImageFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.i("上传图片出错：" + e.getMessage());
                        mView.onUploadImageFailed();
                    }
                });
    }
}
