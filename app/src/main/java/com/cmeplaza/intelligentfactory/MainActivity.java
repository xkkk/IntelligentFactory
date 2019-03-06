package com.cmeplaza.intelligentfactory;

import android.Manifest;
import android.text.TextUtils;

import com.cme.corelib.CoreLib;
import com.cme.corelib.event.UIEvent;
import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.RepeatHelper;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.cme.coreuimodule.base.utils.CommonDialogUtils;
import com.cme.coreuimodule.base.web.SimpleWebFragment;
import com.cmeplaza.intelligentfactory.login.LoginActivity;
import com.cmeplaza.intelligentfactory.utils.UserInfoUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.vector.update_app.update.UpdateUtils;

public class MainActivity extends CommonBaseActivity {
    private SimpleWebFragment simpleWebFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //设置底部导航栏为透明
        String url = CoreLib.BASE_H5_URL;
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        simpleWebFragment = SimpleWebFragment.newInstance(url, "");
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, simpleWebFragment).commit();
        simpleWebFragment.setUserVisibleHint(true);
    }

    @Override
    protected void initData() {
        requestStoragePermissions();
    }

    @Override
    public void onUiEvent(UIEvent uiEvent) {
        super.onUiEvent(uiEvent);
        switch (uiEvent.getEvent()) {
            case UIEvent.EVENT_RE_LOGIN:
                goLoginPage();
                break;
        }
    }

    private void goLoginPage() {
        nextPage(LoginActivity.class, true);
        UserInfoUtils.clearUserInfo();
    }

    @Override
    public void onBackPressed() {
        if (simpleWebFragment != null) {
            if (!simpleWebFragment.onBackPressed()) {
                if (RepeatHelper.isFastDoubleAction()) {
                    super.onBackPressed();
                } else {
                    UiUtil.showToast(R.string.repeat_to_finish);
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    private void requestStoragePermissions() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            checkUpdate();
                        } else {
                            CommonDialogUtils.showSetPermissionDialog(MainActivity.this, getString(R.string.downloadPermissionTip),
                                    null, null);
                        }
                    }
                });
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        new UpdateUtils().checkUpdate(this, false);
    }
}
