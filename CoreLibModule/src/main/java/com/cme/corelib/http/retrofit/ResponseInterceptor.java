package com.cme.corelib.http.retrofit;

import android.text.TextUtils;
import android.text.format.Formatter;

import com.cme.corelib.CoreLib;
import com.cme.corelib.bean.BaseModule;
import com.cme.corelib.http.Methods;
import com.cme.corelib.http.exception.NoNetException;
import com.cme.corelib.http.exception.NotLoginException;
import com.cme.corelib.secret.CoreConstant;
import com.cme.corelib.utils.GsonUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.NetworkUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2016-11-28
 * <p>
 * 网络请求拦截器
 */

public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtils.i("request: " + request.toString());
        Headers requestHeaders = request.headers();
        LogUtils.i("request headers: " + requestHeaders);
        try {
            if (!NetworkUtils.isAvailable(CoreLib.getContext())) {
                throw new NoNetException();
            }
            // try the request
            Response originalResponse = chain.proceed(request);
            if (request.toString().contains(Methods.methods_2)) {
                Headers headers = originalResponse.headers();
                List<String> cookies = headers.values("Set-Cookie");
                if (cookies.size() > 0) {
                    String session = cookies.get(0);
                    String sessionStr = session.substring(0, session.indexOf(";"));
                    LogUtils.i("session is  :" + sessionStr);
                    SharedPreferencesUtil.getInstance().put(CoreConstant.COOKIE, sessionStr);
                }
            } else {
                ResponseBody responseBody = originalResponse.body();
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                Charset charset = Charset.forName("UTF8");
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                String bodyString = buffer.clone().readString(charset);
                LogUtils.i("返回值大小：" + Formatter.formatFileSize(CoreLib.getContext(), bodyString.getBytes().length));
                LogUtils.logL("bodyString==" + bodyString);
//                BaseModule baseModule = GsonUtils.parseJsonWithGson(bodyString, BaseModule.class);
//                if (baseModule != null && TextUtils.equals(baseModule.getCode(), "50006")) {
//                    throw new NotLoginException("请重新请求");
//                }
            }
            return originalResponse;
        } catch (NoNetException e) {
            return chain.proceed(request);
        }
    }
}
