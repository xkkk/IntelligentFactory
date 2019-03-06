package com.cme.corelib.utils;

import android.text.TextUtils;

import java.util.Comparator;

public abstract class PinyinComparator<T> implements Comparator<T> {
    public abstract int compare(T s1, T s2);

    public int compare(String o1, String o2) {
        if (TextUtils.isEmpty(o1)) {
            return 1;
        }
        if (TextUtils.isEmpty(o2)) {
            return -1;
        }
        if (o1.equals("@") || o2.equals("#")) {
            return -1;
        } else if (o1.equals("#") || o2.equals("@")) {
            return 1;
        } else {
            return o1.compareTo(o2);
        }
    }

}
