package com.vector.update_app.update;

import android.support.annotation.NonNull;

import com.cme.corelib.http.CommonHttpUtils;
import com.cme.corelib.http.Methods;
import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.GsonUtils;
import com.cme.corelib.utils.LogUtils;
import com.vector.update_app.HttpManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vector
 * on 2017/6/19 0019.
 */

public class UpdateAppHttpUtil extends CommonHttpUtils implements HttpManager {
    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        versionUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscribe<VersionBean>() {
                    @Override
                    public void onNext(VersionBean versionBean) {
                        LogUtils.i("获取版本结果：" + versionBean);
                        callBack.onResponse(GsonUtils.parseClassToJson(versionBean));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.i("获取版本结果失败：" + e.getMessage());
                        callBack.onError(e.getMessage());
                    }
                });
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {

    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull final String path, @NonNull final String fileName, @NonNull final FileCallback callback) {
        File targetFile = new File(path, fileName);
        if (targetFile.exists()) {
            targetFile.deleteOnExit();
        }
        OkHttpUtils.get()
                .addHeader("Accept-Encoding", "identity")
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onBefore();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        callback.onError(validateError(e, response));
                        File localFile = new File(path, fileName);
                        if (localFile.exists()) {
                            localFile.deleteOnExit();
                        }
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callback.onResponse(response);

                    }
                });
    }

    /**
     * 版本更新
     */
    public static Observable<VersionBean> versionUpdate() {
        Map<String, String> map = getGetMap(Methods.method_get_version, true);
        map.put("appCode", "FACTORYAPP");
        return getStarObservable(map, VersionBean.class);
    }
}