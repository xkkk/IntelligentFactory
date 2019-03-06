package com.cme.coreuimodule.base.widget.edittext;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 自定义EditText 监听系统粘贴动作
 *      使用方法：使用自定义的ListenPasteEditText
 *                activity中需要实现IClipCallBack 接口
 *
 * Created by xiaozi on 2018/3/3.
 */

public class ListenPasteEditText extends android.support.v7.widget.AppCompatEditText {

    private static final String TAG = "Listen2PasteEditText";

    private Context mContext;

    public ListenPasteEditText(Context context) {
        super(context);
        mContext = context;
    }

    public ListenPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.cut:
                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onCut(null);
                }
                break;
            case android.R.id.copy:
                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onCopy(null);
                }
                break;
            case android.R.id.paste:
                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onPaste(null);
                }
                break;
        }
        return super.onTextContextMenuItem(id);
    }
}
