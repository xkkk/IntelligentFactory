package com.cme.corelib.http;

import android.text.TextUtils;

import com.cme.corelib.CoreLib;
import com.cme.corelib.http.retrofit.Api;
import com.cme.corelib.http.retrofit.CommonApiService;
import com.cme.corelib.http.retrofit.StarObservable;
import com.cme.corelib.secret.CoreConstant;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/4.
 * 网络请求工具类
 */

public class CommonHttpUtils {
    // 获取区域parentId
    public static final String TOKEN = "token";
    protected static final String METHOD = "method";
    private static final String USER_ID = "userId";

    protected static Map<String, String> getGetMap(String method, boolean hasSession) {
        Map<String, String> result = new HashMap<>();
        result.put(METHOD, method);
        return result;
    }

    /**
     * @param parameter 请求参数
     * @param mType     返回值类型
     */
    protected static <T> Observable<T> getStarObservable(Map<String, String> parameter, Type mType) {
        Map<String, String> headers = new HashMap<>();
        return getStarObservable(parameter, headers, mType);
    }

    /**
     *
     */
    protected static <T> Observable<T> getStarObservable(Map<String, String> parameter, Map<String, String> headers, Type mType) {
        String session = SharedPreferencesUtil.getInstance().get(CoreConstant.COOKIE);
        if (!TextUtils.isEmpty(session)) {
            headers.put(CoreConstant.COOKIE, session);
        }
        return Observable.create(new StarObservable<T>(createObservableGetWithHeader(parameter, headers), mType));
    }

    /**
     * @param params map参数
     * @return 使用GET请求
     */
    protected static Observable<ResponseBody> createObservableGetWithHeader(Map<String, String> params, Map<String, String> headers) {
        String urlPath = params.get(METHOD);
        params.remove(METHOD);
        return Api.getInstance(CoreLib.getBaseUrl())
                .getApiService()
                .methodGet(CoreLib.PROJECT_NAME + urlPath, params, headers);
    }

    /**
     * @param params map参数
     * @return 使用GET请求
     */
    protected static Observable<ResponseBody> createObservableGet(Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        return createObservableGetWithHeader(params, headers);
    }

    /**
     * 上传文件，图片
     */
    public static Observable<String> uploadFile(String filePath, String fileName) {
        StringBuilder uploadUrl = new StringBuilder(CoreLib.BASE_FILE_URL)
                .append(Methods.image_upload)
                .append("?")
                .append(TOKEN)
                .append("=")
                .append(CoreLib.getSession());
        // 创建文件
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        CommonApiService apiService = Api.newApiInstance(CoreLib.BASE_FILE_URL).getApiService();
        return apiService.uploadFile(uploadUrl.toString(), body);
    }

    /**
     * @param parameter 请求参数
     * @param mType     返回值类型
     */
    protected static <T> Observable<T> getStarObservable(String url, Map<String, String> parameter, Type mType) {
        return Observable.create(new StarObservable<T>(createObservableByPost(url, parameter), mType));
    }

    private static Observable<ResponseBody> createObservableByPost(String url, Map<String, String> params) {
        if (params.containsKey(METHOD)) {
            params.remove(METHOD);
        }
        return Api.getInstance(CoreLib.getBaseUrl())
                .getApiService()
                .methodPost(CoreLib.PROJECT_NAME + url, params);
    }
}
