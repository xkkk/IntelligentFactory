package com.cmeplaza.intelligentfactory.utils;

import android.text.TextUtils;

import com.cme.corelib.bean.BaseModule;
import com.cme.corelib.http.CommonHttpUtils;
import com.cme.corelib.http.Methods;
import com.cme.corelib.secret.CoreConstant;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cmeplaza.intelligentfactory.login.bean.PersonalInfoBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by klx on 2018/5/11.
 * 网络请求工具类
 */

public class HttpUtils extends CommonHttpUtils {
    /**
     * 1、获取短信验证码
     *
     * @param mobile 手机号
     */
    public static Observable<BaseModule> getSMSCode(String mobile) {
        Map<String, String> map = getGetMap(Methods.methods_1, false);
        map.put("mobile", mobile);
        return getStarObservable(map, BaseModule.class);
    }

    /**
     * 2、获取图形验证码
     */
    public static Observable<ResponseBody> getCheckVercode() {
        Map<String, String> map = getGetMap(Methods.methods_2, false);
        String session = SharedPreferencesUtil.getInstance().get(CoreConstant.COOKIE);
        Map<String,String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(session)) {
            headers.put(CoreConstant.COOKIE, session);
        }
        return createObservableGetWithHeader(map,headers);
    }

    /**
     * 3、登录
     *
     * @param mobile    手机号
     * @param vCode     短信验证码
     * @param checkCode 图片验证码
     */
    public static Observable<PersonalInfoBean> login(String mobile, String vCode, String checkCode) {
        Map<String, String> map = getGetMap(Methods.methods_3, false);
        map.put("mobile", mobile);
        map.put("vCode", vCode);
//        map.put("checkCode", checkCode);
        Map<String, String> headers = new HashMap<>();
        return getStarObservable(map, headers, PersonalInfoBean.class);
    }

    /**
     * 4、完善用户资料
     *
     * @param card           用户身份证号
     * @param invitationCode 用户的邀请码
     * @param niceName       用户昵称
     * @param trueName       用户真实昵称
     */
    public static Observable<BaseModule> editUserData(String card, String invitationCode, String niceName, String trueName) {
        Map<String, String> map = getGetMap(Methods.methods_4, false);
//        map.put("card", card);
        map.put("invitationCode", invitationCode);
        map.put("niceName", niceName);
//        map.put("trueName", trueName);
        return getStarObservable(map, BaseModule.class);
    }
}
