package com.cme.coreuimodule.base.widget.edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by klx on 2018/6/21.
 * 输入手机号的包裹类
 */

public class PhoneInputWrapper {
    // 特殊下标位置
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;

    private char placeHolder = ' ';  // 手机分割占位符
    private EditText editText;

    public PhoneInputWrapper(EditText editText) {
        this.editText = editText;
    }

    public PhoneInputWrapper(char placeHolder, EditText editText) {
        this.placeHolder = placeHolder;
        this.editText = editText;
    }

    public void apply() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == placeHolder) {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != placeHolder) {
                            sb.insert(sb.length() - 1, placeHolder);
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == placeHolder) {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }

                    editText.setText(sb.toString());
                    editText.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // 获得不包含空格的手机号
    public String getPhoneText() {
        String str = editText.getText().toString();
        return replaceBlank(str);
    }

    public String replaceBlank(String str) {
        return str.replaceAll(String.valueOf(placeHolder), "");
    }
}
