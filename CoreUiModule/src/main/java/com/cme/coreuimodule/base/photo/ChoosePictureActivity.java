package com.cme.coreuimodule.base.photo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SizeUtils;
import com.cme.corelib.utils.StringUtils;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.common.coreuimodule.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ChoosePictureActivity extends CommonBaseActivity implements ListImageDirPopupWindow.OnImageDirSelected, View.OnClickListener {

    private ProgressDialog mProgressDialog;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File maxCountImgDir;
    private File mImgDir;  // 当前选中的图片路径
    /**
     * 最多图片路径的集合
     */
    private List<String> mImgs;
    private List<String> mVideos; // 视频路径的集合

    private GridView mGirdView;
    private MyAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders = new ArrayList<>();

    private RelativeLayout mBottomLy;

    private TextView mChooseDir;
    private TextView mImageCount;
    private TextView tv_title_right;
    int totalCount = 0;

    private int max = 1;

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            data2View();
            initListDirPopupWindwDefault();
            showDefaultSelect();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            UiUtil.showToast(getApplicationContext(), "一张图片没扫描到");
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());
        mAdapter = new MyAdapter(getApplicationContext(), mImgs, mImgDir.getAbsolutePath(), max);
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(getString(R.string.unit_zhang, mImgs.size()));
        mChooseDir.setText(mImgDir.getName());
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void updateListDirPopupWindow() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFolders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.ic_rong_de_ph_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindwDefault() {
        // 高度手动换算 44dp
        tv_titleHeight = SizeUtils.dp2px(this, 110);

        int dialogHeight = mScreenHeight - tv_titleHeight - mBottomLyHeight
                - getStatusBarHeight();

        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, dialogHeight, mImageFolders,
                LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.ic_rong_de_ph_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ic_rong_de_ph_choosepic;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    MineThread mThread;

    class MineThread extends Thread {

        @Override
        public void run() {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = ChoosePictureActivity.this
                    .getContentResolver();

            // 只查询jpeg和png的图片  ,"video/mp4","video/avi","video/3gpp"
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + " in(?,?,?)",
                    new String[]{"image/jpeg", "image/png", "image/bmp"},
                    MediaStore.Images.Media.DATE_MODIFIED + " desc");
            // 查询视频
            Cursor videoCursor = getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            getDataFormCursor(mCursor,false);
            getDataFormCursor(videoCursor,true);

            if (mCursor != null) {
                mCursor.close();
            }
            if (videoCursor != null) {
                videoCursor.close();
            }
            // 扫描完成，辅助的HashSet也就可以释放内存了
            mDirPaths = null;
            // 通知Handler扫描图片完成
            mHandler.sendEmptyMessage(0x110);
        }
    }

    private void getDataFormCursor(Cursor mCursor,boolean isVideo) {
        String firstImage = null;
        while (mCursor != null && mCursor.moveToNext()) {
            // 获取图片的路径
            String path;
            if (isVideo) {
                path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Video.Media.DATA));
            }else {
                path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            }

            // 预先验证图片的有效性
            final File file = new File(path);
            if (!file.exists()) {
                continue;
            }

            // 拿到第一张图片的路径
            if (firstImage == null)
                firstImage = path;
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();

            if (parentFile == null)
                continue;
            String dirPath = parentFile.getAbsolutePath();
            ImageFolder imageFolder = null;
            // 利用一个HashSet防止多次扫描同一个文件jia
            if (mDirPaths.contains(dirPath)) {
                continue;
            } else {
                mDirPaths.add(dirPath);
                // 初始化imageFloder
                imageFolder = new ImageFolder();
                imageFolder.setDir(dirPath);
                imageFolder.setFirstImagePath(path);
            }
            if (parentFile.list() == null)
                continue;
            String[] listFiles = parentFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return StringUtils.isPicture(filename) || StringUtils.isVideo(filename);
                }
            });
            int picSize = 0;
            if (listFiles != null) {
                picSize = listFiles.length;
            }
            totalCount += picSize;

            imageFolder.setCount(picSize);
            mImageFolders.add(imageFolder);

            if (picSize > mPicsSize) {
                mPicsSize = picSize;
                maxCountImgDir = parentFile;
                mImgDir = parentFile;
            }
        }
    }

    /**
     * 获取本地所有视频
     */
    public void getLoadMedia() {
        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try {
            if (cursor == null) {
                return;
            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)); // id
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)); // 专辑
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)); // 艺术家
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 显示名称
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                LogUtils.i("cme", "视频信息：displayName: " + displayName + "  title: " + title + " duration: " + duration + "  path: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            UiUtil.showToast(this, "暂无外部存储");
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        mThread = new MineThread();
        mThread.start();

    }

    private int mBottomLyHeight = 10;
    private int tv_titleHeight = 10;

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 初始化View
     */
    protected void initView() {
        max = getIntent().getIntExtra("max", 1);

        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        tv_title_right.setOnClickListener(this);
        ViewTreeObserver vto2 = mBottomLy.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBottomLy.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                mBottomLyHeight = mBottomLy.getHeight();
            }
        });
        // 初始化屏幕数据
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        // 获取images组件
        getImages();
        // 初始化响应事件
        initEvent();

    }

    private void showDefaultSelect() {
        mListImageDirPopupWindow
                .setAnimationStyle(R.style.ic_rong_anim_popup_dir);
        mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = .3f;
        getWindow().setAttributes(lp);

    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListDirPopupWindow();
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.ic_rong_anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(ImageFolder floder) {
        if (floder == null || floder.getDir() == null) {
            return;
        }
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return StringUtils.isPicture(filename);
            }
        }));

        Collections.reverse(mImgs);

        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(getApplicationContext(), mImgs, mImgDir.getAbsolutePath(), max);
        mGirdView.setAdapter(mAdapter);
        mGirdView.setSelection(0);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(getString(R.string.unit_zhang, floder.getCount()));

        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThread = null;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tv_title_right) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("data", mAdapter.getPicList());
            setResult(RESULT_OK, intent);
            finish();
        }
    }


}
