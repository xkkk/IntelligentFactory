package com.cmeplaza.intelligentfactory.login;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.cmeplaza.intelligentfactory.R;

/**
 * Created by xiaozi on 2018/5/10.
 */

public class InformationActivity extends CommonBaseActivity implements View.OnClickListener {

    private EditText et_invit_code;
    private EditText et_id_card;
    private EditText et_name;
    private EditText et_nick_name;
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
        String inviteCode = et_invit_code.getText().toString();
        if (TextUtils.isEmpty(inviteCode)) {
            Toast.makeText(this,"邀请码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String idCard = et_id_card.getText().toString();
        if (TextUtils.isEmpty(idCard)) {
            Toast.makeText(this,"身份证号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String name = et_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,"姓名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String nickName = et_nick_name.getText().toString();
        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(this,"昵称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        next(inviteCode,idCard,name,nickName);
    }

    private void next(String inviteCode, String idCard, String name, String nickName) {

    }
}
