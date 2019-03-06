package com.cme.corelib.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.cme.corelib.CoreLib;
import com.cme.corelibmodule.R;


/**
 * 作者：Android_AJ on 2017/4/11.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 发送验证码的CountDownTimer
 */
public class MyCountDownTimer extends CountDownTimer {
    private TextView target;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView target) {
        super(millisInFuture, countDownInterval);
        this.target = target;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        target.setText(CoreLib.getContext().getString(R.string.resend_verification_code, String.valueOf(millisUntilFinished / 1000)));
    }

    @Override
    public void onFinish() {
        target.setEnabled(true);
        target.setText(CoreLib.getContext().getString(R.string.send_verification_code));
    }
}
