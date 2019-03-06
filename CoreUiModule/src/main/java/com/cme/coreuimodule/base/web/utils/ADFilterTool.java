package com.cme.coreuimodule.base.web.utils;

import android.content.Context;
import android.content.res.Resources;

import com.common.coreuimodule.R;

/**
 * Created by klx on 2018/6/2.
 * 去除WebView广告的工具类
 */

public class ADFilterTool {
    public static String getClearAdDivJs(Context context) {
        StringBuilder js = new StringBuilder("javascript:");
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.adBlockDiv);
        for (int i = 0; i < adDivs.length; i++) {
            String adDivArray = "adDivArray"+i;
            String adDivArrayFirstChild = "adDivArrayFirstChild"+i;
            js.append("var ")
                    .append(adDivArray)
                    .append("= document.getElementsByClassName('")
                    .append(adDivs[i])
                    .append("');if(")
                    .append(adDivArray)
                    .append(" != null && ")
                    .append(adDivArray)
                    .append(".length >0){")
                    .append("var ")
                    .append(adDivArrayFirstChild)
                    .append("=")
                    .append(adDivArray)
                    .append("[0];if(")
                    .append(adDivArrayFirstChild)
                    .append("!=null){")
                    .append(adDivArrayFirstChild)
                    .append(".parentNode.removeChild(")
                    .append(adDivArrayFirstChild)
                    .append(");}}");
        }
        return js.toString();
    }

    public static String getClearAdDivJsWithId(Context context) {
        StringBuilder js = new StringBuilder("javascript:");
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.adBlockDiv);
        for (int i = 0; i < adDivs.length; i++) {
//            js.append("var adDiv").append(i).append("= document.getElementById('")
            js.append("var adDiv").append(i).append("= document.getElementsByClassName('")
                    .append(adDivs[i])
                    .append("');if(adDiv")
                    .append(i)
                    .append(" != null)adDiv")
                    .append(i)
                    .append(".parentNode.removeChild(adDiv")
                    .append(i)
                    .append(");");
        }
        return js.toString();
    }
}
