package com.cme.corelib.http.retrofit;

import android.text.TextUtils;

import com.cme.corelib.utils.LogUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xk on 17/6/4.
 */
public class StarObservable<T> implements Observable.OnSubscribe<T> {

    private Type mType;
    private Observable<ResponseBody> observable;

    /**
     * @param observable 请求
     * @param mType      返回值的类型
     */
    public StarObservable(Observable<ResponseBody> observable, Type mType) {
        this.observable = observable;
        this.mType = mType;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody typedString) {
                        try {
                            String responseStr = typedString.string();
                            if (!TextUtils.isEmpty(responseStr)) {
                                LogUtils.i(responseStr);
                                if (TextUtils.equals(responseStr, "{}")) {
                                    subscriber.onNext(null);
                                    subscriber.onCompleted();
                                    return;
                                }
                                if (responseStr.startsWith("{")) {
                                    JsonObject json = new JsonParser().parse(responseStr).getAsJsonObject();

                                    //成功直接返回data,或者{};失败返回error
                                    boolean isSuccess = !responseStr.contains("error");
                                    if (isSuccess) {
                                        T t = new GsonBuilder().serializeNulls().create()
                                                .fromJson(responseStr, mType);
                                        subscriber.onNext(t);
                                    } else {
                                        String code = "";
                                        String msg = "";
                                        JsonObject errorJsonObject = json.getAsJsonObject("error");
                                        if (errorJsonObject.has("code") && errorJsonObject.get("code") != null)
                                            try {
                                                code = errorJsonObject.get("code").getAsString();
                                            } catch (Exception e) {
                                                code = "404";
                                                e.printStackTrace();
                                            }
                                        if (errorJsonObject.has("msg"))
                                            msg = errorJsonObject.get("msg").getAsString();
                                        subscriber.onError(new HttpThrowable(code, msg));
                                    }
                                } else if (responseStr.startsWith("[")) {
                                    //为了兼容服务器 直接返回data的数据为JsonArray
                                    StringBuilder sb = new StringBuilder("{" +
                                            "\"code\":1," +
                                            "\"message\":\"success\"," +
                                            "\"data\":");
                                    sb.append(responseStr);
                                    sb.append("}");
                                    LogUtils.i("StarObservable", sb.toString());
                                    T t = new GsonBuilder().serializeNulls().create()
                                            .fromJson(sb.toString(), mType);
                                    subscriber.onNext(t);
                                }
                            } else {
                                subscriber.onError(new HttpThrowable("request_error", "没有返回值"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(new Throwable(e.getMessage()));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.i("操作失败，异常是：" + throwable + "  异常内容是：" + throwable.getMessage());
                        if (throwable instanceof HttpException) {
                            subscriber.onError(new Throwable("服务暂不可用"));
                        } else if (throwable instanceof IOException) {
                            subscriber.onError(new Throwable("连接失败"));
                        } else {
                            subscriber.onError(throwable);
                        }
                    }
                });
    }
}
