package com.cme.corelib.http.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 网络请求service
 */

public interface CommonApiService {

    @GET()
    Observable<ResponseBody> methodGet(@Url String url, @QueryMap Map<String, String> map);

    @GET()
    Observable<ResponseBody> methodGet(@Url String url, @QueryMap Map<String, String> map, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> methodPost(@Url String url, @FieldMap Map<String, String> map);

    @Multipart
    @POST
    Observable<String> uploadFile(@Url String url, @Part MultipartBody.Part file);
}
