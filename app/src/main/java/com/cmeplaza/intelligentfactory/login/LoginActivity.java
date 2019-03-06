package com.cmeplaza.intelligentfactory.login;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.RegularUtils;
import com.cme.corelib.utils.ScreenUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.activity.MyBaseRxActivity;
import com.cme.coreuimodule.base.web.SimpleWebActivity;
import com.cme.coreuimodule.base.widget.SendVerifyCodeView;
import com.cme.coreuimodule.base.widget.edittext.ZpPhoneEditText;
import com.cmeplaza.intelligentfactory.MainActivity;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.login.contract.LoginContract;
import com.cmeplaza.intelligentfactory.login.presenter.LoginPresenter;
import com.cmeplaza.intelligentfactory.utils.AppConstant;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Func2;

/**
 * 登录页面
 */
public class LoginActivity extends MyBaseRxActivity<LoginPresenter> implements LoginContract.IView {
    final String BaseUrl = "http://id.cmeplaza.com/cme-smart-app/app/";
    final String protocol = "to-agreement-page";

    @BindView(R.id.et_phone)
    ZpPhoneEditText et_phone; // 手机号
    @BindView(R.id.et_sms_verify_code)
    EditText et_sms_verify_code;  // 验证码
    @BindView(R.id.tv_get_sms_verify_code)
    SendVerifyCodeView tv_get_sms_verify_code;
    @BindView(R.id.tv_protocol)
    TextView tv_protocol; // 用户协议
    @BindView(R.id.checkbox_protocol)
    CheckBox checkbox_protocol;
    @BindView(R.id.tv_login)
    TextView tv_login;

    private boolean isEnable = false;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tv_get_sms_verify_code.bindEditText(et_phone);

        Observable<CharSequence> phoneObservable = RxTextView.textChanges(et_phone);
        Observable<CharSequence> smsObservable = RxTextView.textChanges(et_sms_verify_code);

        Observable.combineLatest(phoneObservable, smsObservable,
                new Func2<CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence phoneNumber, CharSequence smsVerifyCode) {
                        return !TextUtils.isEmpty(phoneNumber)
                                && RegularUtils.isMobileSimple(et_phone.getPhoneText())
                                && !TextUtils.isEmpty(smsVerifyCode);
                    }
                })
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (isEnable == aBoolean) {
                            return;
                        }
                        isEnable = aBoolean;
                        if (aBoolean && checkbox_protocol.isChecked()) {
                            tv_login.setEnabled(true);
                            tv_login.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            tv_login.setEnabled(false);
                            tv_login.setTextColor(getResources().getColor(R.color.global_text_color_hint));
                        }
                    }
                });

        checkbox_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(et_phone.getPhoneText())
                            && RegularUtils.isMobileSimple(et_phone.getPhoneText())
                            && !TextUtils.isEmpty(et_sms_verify_code.getText().toString().trim())) {
                        tv_login.setEnabled(true);
                        tv_login.setTextColor(getResources().getColor(R.color.white));
                    }
                    tv_protocol.setTextColor(Color.parseColor("#2ce1ca"));
                } else {
                    tv_login.setEnabled(false);
                    tv_login.setTextColor(getResources().getColor(R.color.global_text_color_hint));
                    tv_protocol.setTextColor(getResources().getColor(R.color.global_text_color_hint));
                }
            }
        });


        tv_get_sms_verify_code.setGetPhoneListener(new SendVerifyCodeView.GetPhoneListener() {
            @Override
            public String getPhone() {
                return et_phone.getPhoneText();
            }
        });
    }

    @Override
    protected void initData() {
        String mobilePhone = SharedPreferencesUtil.getInstance().get(AppConstant.SpConstant.MOBILE_PHONE);
        if (!TextUtils.isEmpty(mobilePhone)) {
            et_phone.setText(mobilePhone);
            et_phone.setSelection(et_phone.getText().toString().trim().length());
        }
        getImageVerifyCode();
    }

    private void getImageVerifyCode() {
        mPresenter.getImageVerifyCode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenUtils.hideSoftInput(et_phone);
    }

    @OnClick({R.id.tv_login, R.id.tv_get_sms_verify_code, R.id.iv_clear_phone, R.id.iv_clear_verify_code, R.id.tv_protocol})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:  // 登录
                mPresenter.login(et_phone.getPhoneText(), et_sms_verify_code.getText().toString().trim(), "");
                break;
            case R.id.tv_get_sms_verify_code:  // 获取短信验证码
                if (tv_get_sms_verify_code.check()) {
                    mPresenter.getSMSVerifyCode(et_phone.getPhoneText());
                    tv_get_sms_verify_code.onSending();
                }
                break;
            case R.id.iv_clear_phone:  // 清空手机号
                et_phone.setText("");
                break;
            case R.id.iv_clear_verify_code: // 清空验证码
                et_sms_verify_code.setText("");
                break;
            case R.id.tv_protocol:  // 服务协议
                SimpleWebActivity.startActivity(LoginActivity.this, BaseUrl + protocol, "服务协议");
                break;
        }
    }

    @Override
    public void onSendSMSVerifyCodeResult(boolean result) {
        if (result) {
            UiUtil.showToast("发送成功");
            tv_get_sms_verify_code.onSendSuccess();
        } else {
            tv_get_sms_verify_code.onSendFail();
        }
    }

    @Override
    public void onLoginResult(boolean result, boolean isNeedEditUserInfo) {
        if (isNeedEditUserInfo) {
            nextPage(PerfectUserInfoActivity.class, false);
        } else {
            nextPage(MainActivity.class, true);
        }
    }

    @Override
    public void onGetPicVerifyResult(byte[] inputStream) {
    }
}
