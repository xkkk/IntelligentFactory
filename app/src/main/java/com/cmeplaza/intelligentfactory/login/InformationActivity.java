package com.cmeplaza.intelligentfactory.login;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cme.corelib.CoreLib;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.utils.AppConstant;
import com.cmeplaza.intelligentfactory.utils.AppStringUtils;

/**
 * Created by xiaozi on 2018/5/10.
 */

public class InformationActivity extends CommonBaseActivity implements View.OnClickListener {

    private EditText et_invit_code;
    private EditText et_id_card;
    private EditText et_name;
    private EditText et_nick_name;
    private EditText et_url;
    private TextView tv_open_intelligent_factroy;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    protected void initData() {
        super.initData();
    }
    @Override
    protected void initView() {
        et_invit_code = (EditText) findViewById(R.id.et_invite_code);
        et_id_card = (EditText) findViewById(R.id.et_card);
        et_name = (EditText) findViewById(R.id.name);
        et_nick_name = (EditText) findViewById(R.id.et_nick_name);
        tv_open_intelligent_factroy = (TextView) findViewById(R.id.tv_open_intelligent_factory);
        et_url = (EditText) findViewById(R.id.et_url);
        tv_open_intelligent_factroy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_intelligent_factory://开启我的智能工厂
                verify();
                break;
        }
    }

    private void verify() {
      String url = et_url.getText().toString();
      if(!TextUtils.isEmpty(url))
        AppStringUtils.setValue("URL_H5",url);
        CoreLib.initNet(AppConstant.BASE_URL, AppConstant.BASE_H5_URL);
    }

}
