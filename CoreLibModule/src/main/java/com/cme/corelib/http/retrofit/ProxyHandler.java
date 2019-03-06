package com.cme.corelib.http.retrofit;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

import com.cme.corelib.CoreLib;
import com.cme.corelib.event.UIEvent;
import com.cme.corelib.http.CommonHttpUtils;
import com.cme.corelib.http.exception.NotLoginException;
import com.cme.corelib.utils.NetworkUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import retrofit2.http.FieldMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
public class ProxyHandler implements InvocationHandler {
    private final static int REFRESH_TOKEN_VALID_TIME = 1000;
    private static long tokenChangedTime = 0;
    int cacheTime = 10;
    public CacheControl FORCE_CACHE1 = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(cacheTime, TimeUnit.SECONDS)//CacheControl.FORCE_CACHE--是int型最大值
            .build();
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;
    private Object mProxyObject;
    private HashMap<String, String> hashMap = new HashMap<>();

    public ProxyHandler(Object proxyObject) {
        mProxyObject = proxyObject;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable.just(null)
                .flatMap(new Func1<Object, Observable<?>>() {
                    @Override
                    public Observable<?> call(Object o) {
                        if (NetworkUtils.isAvailable(CoreLib.getContext())) {
                            try {
                                if (mIsTokenNeedRefresh) {
                                    updateMethodToken(method, args);
                                }
                                return (Observable<?>) method.invoke(mProxyObject, args);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } else {
                            return Observable.error(new NetworkErrorException());
                        }
                        return null;
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (throwable instanceof NotLoginException) {
                                    return refreshTokenWhenTokenInvalid();
                                } else if (throwable instanceof NetworkErrorException) {
                                    return Observable.error(throwable);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                });
    }

    private void updateMethodToken(Method method, Object[] args) {
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(CoreLib.getSession())) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof FieldMap) {
                            HashMap<String, String> hashMap = (HashMap<String, String>) args[i];
                            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                                if (stringStringEntry.getKey().equals(CommonHttpUtils.TOKEN)) {
                                    hashMap.put(CommonHttpUtils.TOKEN, CoreLib.getSession());
                                }
                            }
                            args[i] = hashMap;
                        }
                        //Get请求遍历方式
                        if (annotation instanceof Query) {
                            if (CommonHttpUtils.TOKEN.equals(((Query) annotation).value())) {
                                args[i] = CoreLib.getSession();
                            }
                        }
                        if (annotation instanceof QueryMap) {
                            HashMap<String, String> hashMap = (HashMap<String, String>) args[i];
                            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                                if (stringStringEntry.getKey().equals(CommonHttpUtils.TOKEN)) {
                                    hashMap.put(CommonHttpUtils.TOKEN, CoreLib.getSession());
                                }
                            }
                            args[i] = hashMap;
                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                return Observable.just(true);
            } else {
                tokenChangedTime = new Date().getTime();
                String username = SharedPreferencesUtil.getInstance().get("username");
                String password = SharedPreferencesUtil.getInstance().get("password");
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    new UIEvent(UIEvent.EVENT_RE_LOGIN).post();
                } else {
                    new UIEvent(UIEvent.EVENT_RE_LOGIN).post();
                }
                if (mRefreshTokenError != null) {
                    return Observable.error(mRefreshTokenError);
                } else {
                    return Observable.just(true);
                }
            }
        }
    }
}
