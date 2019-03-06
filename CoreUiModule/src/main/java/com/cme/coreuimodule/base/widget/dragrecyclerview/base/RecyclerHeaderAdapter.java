package com.cme.coreuimodule.base.widget.dragrecyclerview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by klx on 2017/11/29.
 *
 */

public abstract class RecyclerHeaderAdapter<T> extends RecyclerView.Adapter<RecyclerHeaderAdapter.BaseViewHolder> {
    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    public static final int TYPE_NORMAL = 0;

    protected Context mContext;
    protected List<T> mDatas;
    protected ItemTypeSupport itemTypeSupport;
    protected LayoutInflater layoutInflater;

    public RecyclerHeaderAdapter(Context context, List<T> mDatas, ItemTypeSupport<T> itemTypeSupport) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.itemTypeSupport = itemTypeSupport;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(itemTypeSupport.getLayoutId(viewType), parent,false);
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(rootView);
            case TYPE_FOOTER:
                return new FooterViewHolder(rootView);
            case TYPE_NORMAL:
            default:
                return new ItemViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T data = mDatas.get(position);
        switch (itemTypeSupport.getItemViewType(position, data)) {
            case TYPE_HEADER:
                convertHeader((HeaderViewHolder) holder, data, position);
                break;
            case TYPE_FOOTER:
                convertFooter((FooterViewHolder) holder, data, position);
                break;
            case TYPE_NORMAL:
            default:
                convert((ItemViewHolder) holder, data, position);
        }
    }

    protected abstract void convert(ItemViewHolder holder, T t, int position);

    protected abstract void convertHeader(HeaderViewHolder holder, T t, int position);

    protected abstract void convertFooter(FooterViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemTypeSupport.getItemViewType(position,mDatas.get(position));
    }

    public static class HeaderViewHolder extends BaseViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterViewHolder extends BaseViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends BaseViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        protected View rootView;
        private SparseArray<View> mViews;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            mViews = new SparseArray<>();
        }

        public View getRootView() {
            return rootView;
        }

        /**
         * 通过viewId获取控件
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = rootView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }
}
