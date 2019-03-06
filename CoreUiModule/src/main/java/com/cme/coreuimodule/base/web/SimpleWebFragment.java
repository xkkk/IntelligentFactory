package com.cme.coreuimodule.base.web;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cme.corelib.CoreLib;
import com.cme.corelib.event.UIEvent;
import com.cme.corelib.secret.CoreConstant;
import com.cme.corelib.utils.CommonUtils;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.coreuimodule.base.activity.MyBaseRxFragment;
import com.cme.coreuimodule.base.mvp.BaseContract;
import com.cme.coreuimodule.base.utils.TextCommon;
import com.cme.coreuimodule.base.web.utils.ADFilterTool;
import com.common.coreuimodule.R;
import com.just.agentweb.AgentWeb;

import java.lang.ref.WeakReference;
import java.net.URLDecoder;

/**
 * Created by klx on 2018/1/12.
 * webFragment
 */

public class SimpleWebFragment extends MyBaseRxFragment {
    public static final String FROM_LOAD_URL = "from_load_url";
    public static final String TITLE_NAME = "title_name";
    private static final int REQUEST_CODE = 0x001;
    private static final int FILTER_AD = 0x001;
    private static final String TAG = "SimpleWebFragment";
    LinearLayout ll_rootView;
    RelativeLayout rl_no_net;
    RelativeLayout commonTitle;
    ImageView iv_close;
    TextView tv_close;
    private AgentWeb mAgentWeb;

    private String fromUrl = "";
    private String fromTitle = "";
    private boolean isReload = false;
    private String isRead = "";// 判断消息是否一直有未读

    private boolean isHome = true;

        // 去除广告
    private boolean isClose;
    private FilterAdHandler filterAdHandler;

    public static SimpleWebFragment newInstance(String url) {
        return newInstance(url, "");
    }

    public static SimpleWebFragment newInstance(String url, String name) {
        SimpleWebFragment simpleWebFragment = new SimpleWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM_LOAD_URL, url);
        bundle.putString(TITLE_NAME, name);
        simpleWebFragment.setArguments(bundle);
        return simpleWebFragment;
    }

    @Override
    protected BaseContract.BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_web;
    }

    @Override
    protected void initView() {
        filterAdHandler = new FilterAdHandler(this);
        ll_rootView = (LinearLayout) rootView.findViewById(R.id.ll_rootView);
        commonTitle = (RelativeLayout) rootView.findViewById(R.id.commonTitle);
        iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        tv_close = (TextView) rootView.findViewById(R.id.tv_close);
        rl_no_net = (RelativeLayout) rootView.findViewById(R.id.rl_no_net);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fromUrl = bundle.getString(FROM_LOAD_URL);
            fromTitle = bundle.getString(TITLE_NAME);
            if (!TextUtils.isEmpty(fromTitle)) {
                RelativeLayout commonTitle = (RelativeLayout) rootView.findViewById(R.id.commonTitle);
                visible(commonTitle);
                tv_close.setVisibility(View.VISIBLE);
                setTitleCenter(fromTitle);
            }
        }
        tv_close.setOnClickListener(new View.OnClickListener() {//关闭页面
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        rl_no_net.setOnClickListener(new View.OnClickListener() {//重新加载
            @Override
            public void onClick(View v) {
                mAgentWeb.getWebCreator().getWebView().reload();
                if (TextUtils.isEmpty(fromTitle)) {
                    commonTitle.setVisibility(View.GONE);
                }
                rl_no_net.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(fromUrl)) {
            getActivity().finish();
        } else {
            try {
                LogUtils.i("cme", "要打开的连接：  " + fromUrl);
                mAgentWeb = AgentWeb.with(getActivity())//传入Activity or Fragment
                        .setAgentWebParent(ll_rootView, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                        .useDefaultIndicator()// 使用默认进度条
                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                        .createAgentWeb()
                        .ready()  //此方法重置了WebViewClient ChromeClient 如果自定义 需写在初始化之后
                        .go(fromUrl);
                mAgentWeb.getWebCreator().getWebView().setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                mAgentWeb.getWebCreator().getWebView().setWebViewClient(new MyWebViewClient());
                //为防止webview重置 需在mAgentWeb创建之后写
                mAgentWeb.getAgentWebSettings().getWebSettings().setSupportZoom(true);
                mAgentWeb.getAgentWebSettings().getWebSettings().setBuiltInZoomControls(true);
                mAgentWeb.getAgentWebSettings().getWebSettings().setDisplayZoomControls(false);
                // 默认不使用缓存
                mAgentWeb.getAgentWebSettings().getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                mAgentWeb.getJsInterfaceHolder().addJavaObject("cmesmart", new AndroidInterface(mAgentWeb, getActivity()));
                mAgentWeb.getWebCreator().getWebView().setDownloadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                        LogUtils.i("cme", "下载地址：" + url);
                        CommonUtils.openWeb(getActivity(), url);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    public boolean onBackPressed() {
        return mAgentWeb != null && mAgentWeb.back();
    }

    private void onReceiveError() {
        commonTitle.setVisibility(View.VISIBLE);
        rl_no_net.setVisibility(View.VISIBLE);
        isReload = true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.d(TAG, "shouldOverrideUrlLoading() url:" + url);
            if (TextUtils.isEmpty(url)) {
                return true;
            }
            if (url.contains(CoreConstant.H5Constant.redirectFlag)) {
                String redirectPath = TextCommon.getValueByName(url, "redirectUrl");
                redirectPath = URLDecoder.decode(redirectPath);
                LogUtils.d(TAG, "redirectPath : " + redirectPath);
                if (!TextUtils.isEmpty(redirectPath)) {
                    if (redirectPath.contains(CoreConstant.H5Constant.reLogin)) {
                        new UIEvent(UIEvent.EVENT_RE_LOGIN).post();
                    } else {
                        String userId = SharedPreferencesUtil.getInstance().get("user_id");
                        view.loadUrl(CoreLib.BASE_H5_URL + URLDecoder.decode(redirectPath) + "?loginUserId=" + userId);
                    }
                    return true;
                }
            }

            if (url.contains("wifi.shouji.360.cn")) {
                view.loadUrl(fromUrl);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d(TAG, "onPageStarted() url is:" + url);
            if (isReload) {
                mAgentWeb.getWebCreator().getWebView().setVisibility(View.GONE);
            }

            if (isClose) { //如果线程正在运行就不用重新开启一个线程了
                return;
            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    isClose = true;
//                    while (isClose) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        filterAdHandler.sendEmptyMessage(FILTER_AD);
//                    }
//                }
//            }).start();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 打印html代码
//            view.loadUrl("javascript:window.cmesmart.showHtmlCode(document.body.innerHTML);");
            LogUtils.d(TAG, "onPageFinished() url is:" + url);
            isClose = false;
            if (TextUtils.equals(fromUrl, url) || fromUrl.contains(url)) {
                isHome = true;
            } else {
                isHome = false;
            }
            if (isReload) {
                mAgentWeb.getWebCreator().getWebView().setVisibility(View.GONE);
                commonTitle.setVisibility(View.VISIBLE);
                rl_no_net.setVisibility(View.VISIBLE);
            } else {
                mAgentWeb.getWebCreator().getWebView().setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(fromTitle)) {
                    commonTitle.setVisibility(View.GONE);
                }
                rl_no_net.setVisibility(View.GONE);
            }
            isReload = false;
            /* 将cookie保存起来*/
            String c = CookieManager.getInstance().getCookie(url);
//            DataCenter.setCookie(c);
            CookieSyncManager.getInstance().sync();
            if (TextUtils.isEmpty(fromTitle)) {
                setTitleCenter(view.getTitle());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            LogUtils.d(TAG, "onReceivedError() errorCode:" + errorCode + "----failingUrl" + failingUrl);
            onReceiveError();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @TargetApi(21)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            LogUtils.d(TAG, "onReceivedHttpError() errorCode:" + errorResponse.getStatusCode() + " reason: " + errorResponse.getReasonPhrase());
            int statusCode = errorResponse.getStatusCode();
            if (statusCode != 200 && statusCode != 404) {
                onReceiveError();
            }
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }

        private static class FilterAdHandler extends Handler {
        private WeakReference<SimpleWebFragment> simpleWebFragmentWeakReference;

        private FilterAdHandler(SimpleWebFragment simpleWebFragment) {
            this.simpleWebFragmentWeakReference = new WeakReference<>(simpleWebFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FILTER_AD:
                    if (simpleWebFragmentWeakReference.get() != null) {
                        String js = ADFilterTool.getClearAdDivJs(simpleWebFragmentWeakReference.get().getActivity());
                        LogUtils.v(TAG, js);
                        simpleWebFragmentWeakReference.get().mAgentWeb.getWebCreator().getWebView().loadUrl(js);
                    }
                    break;
            }
        }
    }
}
