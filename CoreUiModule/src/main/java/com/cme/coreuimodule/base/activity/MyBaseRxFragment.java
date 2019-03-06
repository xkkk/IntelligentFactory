package com.cme.coreuimodule.base.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;

import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.BitmapLoader;
import com.cme.corelib.utils.CameraUtils;
import com.cme.corelib.utils.FileUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.clipimage.ClipImageActivity;
import com.cme.coreuimodule.base.fragment.BaseLazyFragment;
import com.cme.coreuimodule.base.mvp.BaseContract;
import com.cme.coreuimodule.base.utils.CommonDialogUtils;
import com.common.coreuimodule.R;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by klx on 2017/9/15.
 */

public abstract class MyBaseRxFragment<T extends BaseContract.BasePresenter> extends BaseLazyFragment implements BaseContract.BaseView {

    private static final int REQUEST_CLIP_IMAGE = 2028;
    private static final int REQUEST_CHOOSE_PICTURE_AND_CROP = 0xac22;
    private static final int REQUEST_CHOOSE_CAMERA_AND_CROP = 0xac23;
    private static final int REQUEST_CHOOSE_CAMERA = 0xac24;
    private static String mCameraPicturePath;
    protected T mPresenter;
    protected String uploadImage;

    @Override
    public void showError(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        UiUtil.showToast(message);
    }

    @Override
    public <T> LifecycleTransformer<T> bind() {
        return bindToLifecycle();
    }

    @Override
    public void nextPage(Class clazz, boolean isFinishThisPage) {
        if (getActivity() != null) {
            commonStartActivity(new Intent(getActivity(), clazz));
            if (isFinishThisPage) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void nextPageWithSingleActivity(Class clazz) {
        if (getActivity() != null) {
            Intent loginIntent = new Intent(getActivity(), clazz);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        attachMvpView();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * [此方法不可再重写]
     */
    public final void attachMvpView() {

        if (mPresenter == null) {
            mPresenter = createPresenter();
            if (mPresenter != null) {
                mPresenter.attachView(this);
            }
        }
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    /**
     * 显示选择图片的弹窗
     */
    protected void showChosePicDialog() {
        CommonDialogUtils.showChoosePicDialog(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTakePicPermissions(true);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从相册中选择
                requestChoosePicPermissions();
            }
        });
    }

    public void requestTakePicPermissions(final boolean crop) {
        new RxPermissions(getActivity())
                .request(Manifest.permission.CAMERA)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            takePhoto(crop);
                        } else {
                            CommonDialogUtils.showSetPermissionDialog(getActivity(), getString(R.string.takePicPermissionTip));
                        }
                    }
                });
    }

    public void requestChoosePicPermissions() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            CameraUtils.openPhotos(MyBaseRxFragment.this, REQUEST_CHOOSE_PICTURE_AND_CROP);
                        } else {
                            CommonDialogUtils.showSetPermissionDialog(getActivity(), getString(R.string.choosePicPermissionTip));
                        }
                    }
                });
    }

    private void takePhoto(boolean crop) {
        String dir = getActivity().getExternalCacheDir() + "/baby/Camera/";
        File destDir = new File(dir);
        if (!destDir.exists()) {
            boolean isCreateSuccess = destDir.mkdirs();
            if (!isCreateSuccess) {
                return;
            }
        }
        File file = new File(dir, new DateFormat().format(
                "yyyy_MMdd_hhmmss", Calendar.getInstance(Locale.CHINA))
                + ".jpg");
        mCameraPicturePath = file.getAbsolutePath();
        try {
            CameraUtils.openCamera(this, crop ? REQUEST_CHOOSE_CAMERA_AND_CROP : REQUEST_CHOOSE_CAMERA, file);
        } catch (ActivityNotFoundException anf) {
            anf.printStackTrace();
            UiUtil.showToast(getActivity(), R.string.camera_not_prepared);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("MyBaseRxFragment : onActivityResult");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_CAMERA_AND_CROP:
                    if (TextUtils.isEmpty(mCameraPicturePath)) {
                        UiUtil.showToast(getActivity(), R.string.camera_not_prepared);
                        onChoosePicFailed(getString(R.string.camera_not_prepared));
                    } else {
                        Uri parse = Uri.parse("file://" + mCameraPicturePath);
                        String par = FileUtils.getPath(getActivity(), parse);
                        Uri croppedAvatar = Uri.fromFile(new File(getActivity().getCacheDir(), "croppedAvatar"));
                        String photoPath = FileUtils.getPath(getActivity(), croppedAvatar);
                        ClipImageActivity.prepare()
                                .aspectX(3).aspectY(3)
                                .inputPath(par).outputPath(photoPath)
                                .startForResult(MyBaseRxFragment.this, REQUEST_CLIP_IMAGE);
                    }
                    break;
                case REQUEST_CHOOSE_PICTURE_AND_CROP:
                    if (data != null&&data.getData()!=null) {
                        onChoosePicFailed("");
                    }
                    Uri source = data.getData();
                    String original = FileUtils.getPath(getActivity(), source);
                    if (original != null) {
                        source = Uri.parse("file://" + original);
                    }
                    Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "croppedAvatar"));
                    String url = FileUtils.getPath(getActivity(), destination);
                    ClipImageActivity.prepare()
                            .aspectX(3).aspectY(3)
                            .inputPath(original).outputPath(url)
                            .startForResult(MyBaseRxFragment.this, REQUEST_CLIP_IMAGE);
                    break;
                case REQUEST_CHOOSE_CAMERA:
                    if (TextUtils.isEmpty(mCameraPicturePath)) {
                        UiUtil.showToast(getActivity(), R.string.camera_not_prepared);
                        onChoosePicFailed(getString(R.string.camera_not_prepared));
                    } else {
                        Uri parse = Uri.parse("file://" + mCameraPicturePath);
                        String photoPath = FileUtils.getPath(getActivity(), parse);
                        onChoosePicResult(photoPath, Uri.parse("file://" + photoPath));
                    }
                    break;
                case REQUEST_CLIP_IMAGE://剪切图片
                    Uri avatarUri1 = Uri.fromFile(new File(getActivity().getCacheDir(), "croppedAvatar"));
                    String photoPath1 = FileUtils.getPath(getActivity(), avatarUri1);
                    Bitmap bitmap1 = BitmapLoader.getBitmapFromFile(photoPath1, 720,
                            1280);
                    final String thumbnailPath1 = BitmapLoader.saveBitmapToLocal(getActivity(), bitmap1);
                    onChoosePicResult(thumbnailPath1, Uri.parse("file://" + thumbnailPath1));
                    break;

            }
        } else {
            onChoosePicFailed("");
        }
    }

    protected void onChoosePicResult(String path, Uri result) {
        this.uploadImage = path;
    }

    protected void onChoosePicFailed(String reason){

    }

}
