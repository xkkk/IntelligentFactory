package com.cme.coreuimodule.base.widget.edittext;

/**
 * 自定义复制粘贴剪切方法
 * Created by xiaozi on 2018/3/3.
 */

public interface IClipCallback {
    void onCut(Object o);
    void onCopy(Object o);
    void onPaste(Object o);
}
