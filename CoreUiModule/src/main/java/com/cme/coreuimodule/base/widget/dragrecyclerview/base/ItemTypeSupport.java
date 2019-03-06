package com.cme.coreuimodule.base.widget.dragrecyclerview.base;

public interface ItemTypeSupport<T> {
    int getLayoutId(int type);

    int getViewTypeCount();

    int getItemViewType(int position, T t);

}