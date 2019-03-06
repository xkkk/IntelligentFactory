package com.cme.coreuimodule.base.utils;

import android.text.TextUtils;

import com.common.coreuimodule.R;

/**
 *
 * 根据传入的字符串返回对应的icon id
 * Created by xiaozi on 2018/4/21.
 */

public class IconUtils {
    public static int getDrawable(String s){
        if (!TextUtils.isEmpty(s)) {
           if (s.equals("集团董事长")){
               return R.drawable.icon_boss;
           }else if (s.equals("院长")){
               return R.drawable.icon_leave_2;
           }else if (s.equals("顾问")){
               return R.drawable.icon_leave_3;
           }else if (s.equals("经理")){
               return R.drawable.icon_leave_4;
           }else if (s.equals("副经理")){
               return R.drawable.icon_leave_5;
           }else if (s.equals("总经理")){
               return R.drawable.icon_leave_6;
           }else if (s.equals("主管")){
               return R.drawable.icon_leave_7;
           }else if (s.equals("部门经理")){
               return R.drawable.icon_leave_8;
           }else if (s.equals("董事长")){
               return R.drawable.icon_leave_9;
           }else if (s.equals("分管副总")){
               return R.drawable.icon_leave_10;
           }else if (s.equals("副院长")){
               return R.drawable.icon_leave_11;
           }else if (s.equals("副总经理")){
               return R.drawable.icon_leave_12;
           }else if (s.equals("员工")){
               return R.drawable.icon_leave_13;
           }else if (s.equals("主管副总")){
               return R.drawable.icon_leave_14;
           }else if (s.equals("专业总监")){
               return R.drawable.icon_leave_15;
           }else if (s.equals("总监")){
               return R.drawable.icon_leave_16;
           }
        }else {
            return -1;
        }
        return -1;
    }
}
