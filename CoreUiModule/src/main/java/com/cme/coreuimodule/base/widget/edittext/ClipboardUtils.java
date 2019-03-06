package com.cme.coreuimodule.base.widget.edittext;

import android.content.ClipboardManager;
import android.content.Context;
import com.cme.corelib.utils.LogUtils;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 控制注销系统剪切版 获取剪切板内容
 * Created by xiaozi on 2018/3/3.
 */

public class ClipboardUtils {
    static ClipboardManager manager;
    static Context context;
    static MyClipboardListen clipboardListen;

    private static String content;
    public static void init(Context context1){
        context=context1;
        manager= (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        clipboardListen=new MyClipboardListen();
        manager.addPrimaryClipChangedListener(clipboardListen);
    }
    //获取系统剪切板内容

    static class MyClipboardListen implements ClipboardManager.OnPrimaryClipChangedListener {
        @Override
        public void onPrimaryClipChanged() {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                if (addedText != null) {
                    LogUtils.d( "copied text: " + addedText);
                    content=addedText.toString();
                }
            }
        }
    }
    public static String getContent() {
        return content;
    }
    //接触绑定监听防止内存泄漏 这个方法需写在activity ondestroy方法中
    public static void unbind(){
        if (clipboardListen != null) {
            manager.removePrimaryClipChangedListener(clipboardListen);
        }
    }
}
