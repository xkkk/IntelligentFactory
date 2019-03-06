package com.cme.corelib.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by Mikes on 2016-5-12.
 */
public class CameraUtils {
    private static final String IMAGE_TYPE = "image/*";

    /**
     * 打开照相机，没有照片存储路径
     *
     * @param activity
     * @param requestCode
     */
    public static void openCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开照相机
     *
     * @param activity    当前的activity
     * @param requestCode 拍照成功时activity forResult 的时候的requestCode
     * @param photoFile   拍照完毕时,图片保存的位置
     */
    public static void openCamera(Activity activity, int requestCode,
                                  File photoFile) {
        if (!hasCamera(activity)) {
            throw new ActivityNotFoundException();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开照相机
     *
     * @param fragment    当前的fragment
     * @param requestCode 拍照成功时activity forResult 的时候的requestCode
     * @param photoFile   拍照完毕时,图片保存的位置
     */
    public static void openCamera(Fragment fragment, int requestCode,
                                  File photoFile) {
        if (!hasCamera(fragment.getActivity())) {
            throw new ActivityNotFoundException();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public static boolean hasCamera(Activity mActivity) {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 本地照片调用
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhotos(Activity activity, int requestCode) {
        if (
//                openPhotosNormal(activity, requestCode) &&
                openPhotosBrowser(activity, requestCode)
                        && openPhotosFinally(activity)) ;
    }

        /**
     * 本地照片调用
     *
     * @param fragment
     * @param requestCode
     */
    public static void openPhotos(Fragment fragment, int requestCode) {
        if (
//                openPhotosNormal(activity, requestCode) &&
                openPhotosBrowser(fragment, requestCode)
                        && openPhotosFinally(fragment.getActivity())) ;
    }

    /**
     * 打开本地相册.
     */
    private static boolean openPhotosNormal(Activity activity, int actResultCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        try {
            activity.startActivityForResult(intent, actResultCode);
        } catch (ActivityNotFoundException e) {
            return true;
        }
        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private static boolean openPhotosBrowser(Activity activity, int requestCode) {
//        Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE);
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            activity.startActivityForResult(wrapperIntent, requestCode);
        } catch (ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

        /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private static boolean openPhotosBrowser(Fragment fragment, int requestCode) {
//        Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE);
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            fragment.startActivityForResult(wrapperIntent, requestCode);
        } catch (ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    private static boolean openPhotosFinally(Activity activity) {
        Toast.makeText(activity, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG)
                .show();
        return false;
    }
}
