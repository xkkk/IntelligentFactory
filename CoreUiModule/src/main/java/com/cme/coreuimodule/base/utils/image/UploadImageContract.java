package com.cme.coreuimodule.base.utils.image;

import com.cme.corelib.utils.image.ImageBean;
import com.cme.coreuimodule.base.mvp.BaseContract;

/**
 * Created by klx on 2017/12/25.
 * 上次图片Contract
 */

public interface UploadImageContract {
    interface IUploadImageView extends BaseContract.BaseView{
        void onUploadImage(ImageBean uploadImageBean);

        void onUploadImageFailed();
    }
    interface IUploadImagePresenter{
        void uploadImage(String filePath);
    }
}
