package com.cmeplaza.intelligentfactory.login;

import android.view.View;
import android.widget.EditText;

import com.cme.corelib.utils.ScreenUtils;
import com.cme.coreuimodule.base.activity.MyBaseRxActivity;
import com.cme.coreuimodule.base.utils.CommonDialogUtils;
import com.cmeplaza.intelligentfactory.MainActivity;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.login.contract.PerfectUserInfoContract;
import com.cmeplaza.intelligentfactory.login.presenter.PerfectUserInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaozi on 2018/5/10.
 * 完善个人信息页面
 */

public class PerfectUserInfoActivity extends MyBaseRxActivity<PerfectUserInfoPresenter> implements PerfectUserInfoContract.IView {
    @BindView(R.id.et_invite_code)
    EditText et_invit_code;
//    @BindView(R.id.et_card)
//    EditText et_id_card;
//    @BindView(R.id.name)
//    EditText et_name;
    @BindView(R.id.et_nick_name)
    EditText et_nick_name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenUtils.hideSoftInput(et_invit_code);
    }

    @Override
    protected PerfectUserInfoPresenter createPresenter() {
        return new PerfectUserInfoPresenter();
    }

    @OnClick({R.id.tv_open_intelligent_factory})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_intelligent_factory:
                verify();
                break;
        }
    }

    private void verify() {
        String inviteCode = et_invit_code.getText().toString();
//        String idCard = et_id_card.getText().toString();
//        String name = et_name.getText().toString();
        String nickName = et_nick_name.getText().toString();
        mPresenter.editUserInfo("", inviteCode, nickName, "");
    }

    @Override
    public void onEditSuccess() {
        nextPageWithSingleActivity(MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        CommonDialogUtils.showConfirmDialog(this, "您还没有完善信息，确认退出？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
