package com.cme.coreuimodule.base.photo;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cme.corelib.image.ImageLoaderManager;
import com.cme.corelib.image.ImageLoaderOptions;
import com.common.coreuimodule.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;


public class ListImageDirPopupWindow extends
        BasePopupWindowForListView<ImageFolder> {
    private ListView mListDir;

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFolder> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(new CommonAdapter<ImageFolder>(context, R.layout.ic_rong_de_ph_list_dir_item, mDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, ImageFolder item, int position) {
                viewHolder.setText(R.id.id_dir_item_name, item.getName());
                if (item.getName().contains("Camera")) {
                    viewHolder.setText(R.id.id_dir_item_name, "手机相册");
                }
                if (item.getName().contains("/Screenshots")) {
                    viewHolder.setText(R.id.id_dir_item_name, "屏幕截图");
                }
                ImageView id_dir_item_image = viewHolder.getView(R.id.id_dir_item_image);
                ImageLoaderOptions options = new ImageLoaderOptions.Builder(id_dir_item_image, "file:///" + item.getFirstImagePath())
                        .build();
                ImageLoaderManager.getInstance().showImage(options);
                viewHolder.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }
        });
    }

    public interface OnImageDirSelected {
        void selected(ImageFolder floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDatas.get(position));
                }
            }
        });
    }

    @Override
    public void init() {

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
    }

}
