package com.cme.corelib.utils.image;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cme.corelib.CoreLib;
import com.cme.corelib.http.Methods;
import com.cme.corelib.image.ImageLoaderManager;
import com.cme.corelib.utils.GsonUtils;
import com.cme.corelib.utils.ImageSize;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SizeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikes on 2016-5-12.
 * 图片工具类
 */
public class BaseImageUtils {
    public static final int IMAGE_TYPE_SMALL = 1;
    public static final int IMAGE_TYPE_MIDDLE = 2;
    public static final int IMAGE_TYPE_NORMAL = 3;

    public static void showImage(ImageView imageView, String url) {
        ImageLoaderManager.getInstance()
                .showImage(BaseImageOptions.getPortraitOptions(imageView,url));
    }

    public static void showImage(ImageView imageView, Integer resourceId) {
        ImageLoaderManager.getInstance()
                .showImage(BaseImageOptions.getNoPlaceHolderOption(imageView,resourceId));
    }

    /**
     * 获取imageUrl
     *
     * @param avatar 头像bean
     * @param type   类型
     * @return
     */
    public static String getImageUrl(String avatar, int type) {
        return getDownloadUrl(getImageId(avatar), type);
    }

    /**
     * 根据图片名称获取图片url
     *
     * @param id 图片id
     * @return 图片url
     */
    public static String getDownloadUrl(String id, int type) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        StringBuilder result = new StringBuilder(CoreLib.BASE_FILE_URL).append(Methods.image_show)
                .append(id);
        switch (type) {
            case IMAGE_TYPE_SMALL:
                result.append("_small");
                break;
            case IMAGE_TYPE_MIDDLE:
                result.append("_middle");
                break;
            case IMAGE_TYPE_NORMAL:
            default:

                break;
        }
        return result.toString();
    }

    public static String getImageId(String avatar) {
        if (TextUtils.isEmpty(avatar) || TextUtils.equals(avatar, "undefined") || TextUtils.equals(avatar, "[undefined]")) {
            return "";
        }
        //如果avatar 不为空 null 对其第一层解析 如果avatar不包含 code 并且 没有[ 表示avatar就是一个普通的bean 或者是一个id
        if (!avatar.contains("code") && !avatar.contains("[")) {
            if (avatar.contains("originalName")) {//如果avater 包含originalName表示是一个bean 用bean解析
                ImageBean.DataBean imageBean = getImageBeanFromString(avatar);
                if (imageBean != null) {
                    return imageBean.getId();
                }
            } else {
                return avatar;
            }
        }

        if (!avatar.contains("code") && avatar.contains("[")) {//表示avatar是一个 [{bean}]
            List<ImageBean.DataBean> list = GsonUtils.parseJsonArrayWithGson(avatar, ImageBean.DataBean.class);
            if (list != null && list.size() > 0) {
                ImageBean.DataBean imageBean = list.get(0);
                if (imageBean != null) {
                    return imageBean.getId();
                }
            }
        }

        if (avatar.contains("code") && avatar.contains("[")) {//表示avatar是一个 {code data [{ bean}]}
            List<ImageBean> portraitBeanList = GsonUtils.parseJsonArrayWithGson(avatar, ImageBean.class);
            if (portraitBeanList != null && portraitBeanList.size() > 0) {
                ImageBean portraitBean = portraitBeanList.get(0);
                if (portraitBean != null && portraitBean.getData() != null) {
                    ImageBean.DataBean dataBean = portraitBean.getData().get(0);
                    if (dataBean != null) {
                        return dataBean.getId();
                    }
                }
            }
        }
        return "";
    }

    public static ImageBean.DataBean getImageBeanFromString(String avatar) {
        if (TextUtils.isEmpty(avatar)) {
            return null;
        }
        // 设置头像
        ImageBean.DataBean imageBean = null;
        if (avatar.contains("[")) {
            List<ImageBean.DataBean> avatars = GsonUtils.parseJsonArrayWithGson(avatar, ImageBean.DataBean.class);
            if (avatars != null && avatars.size() > 0) {
                imageBean = avatars.get(0);
            }
        } else {
            imageBean = GsonUtils.parseJsonWithGson(avatar, ImageBean.DataBean.class);
        }
        return imageBean;
    }

    /**
     * 根据avatar 获取图片地址 目前已知不同类型均有判断
     *
     * @param avatar
     * @return
     */
    public static String getImageUrl(String avatar) {
        return getImageUrl(getImageId(avatar), IMAGE_TYPE_NORMAL);
    }

    /**
     * 根据图片名称获取图片url
     *
     * @param id 图片id
     * @return
     */
    public static String getDownloadUrl(String id) {
        return getDownloadUrl(id, IMAGE_TYPE_NORMAL);
    }

    /**
     * Convert a Bitmap to a byte[].
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    /**
     * Clip a bitmap and return the center part.
     *
     * @param source
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap clipCenter(Bitmap source, int targetWidth, int targetHeight) {
        int startWidth = (source.getWidth() - targetWidth) / 2;
        int startHeight = ((source.getHeight() - targetHeight) / 2);
        Rect src =
                new Rect(startWidth, startHeight, startWidth + targetWidth, startHeight
                        + targetHeight);
        return clipBitmap(source, src);
    }

    /**
     * Clip a Bitmap with a Rect.
     *
     * @param bmp
     * @param src
     * @return
     */
    private static Bitmap clipBitmap(Bitmap bmp, Rect src) {
        int width = src.width();
        int height = src.height();
        Rect des = new Rect(0, 0, width, height);
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);
        canvas.drawBitmap(bmp, src, des, null);
        return croppedImage;
    }

    /**
     * Clip a bitmap and return the bottom part.
     *
     * @param source
     * @param targetHeight
     * @return
     */
    public static Bitmap clipBottom(Bitmap source, int targetHeight) {
        int width = source.getWidth();
        int height = source.getHeight();
        int startWidth = 0;
        int startHeight = height - targetHeight;
        Rect src = new Rect(startWidth, startHeight, 0 + width, startHeight + targetHeight);
        return clipBitmap(source, src);
    }

    /**
     * Zoom a Bitmap.
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImage(Bitmap bm, double newWidth, double newHeight) {
        if (bm == null) {
            return null;
        }
        float width = bm.getWidth();
        float height = bm.getHeight();
        Matrix matrix = new Matrix();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 限制宽度等比例压缩。
     *
     * @param bgimage
     * @param newWidth
     * @return
     */
    public static Bitmap scaleImageByWidth(Bitmap bgimage, float newWidth) {
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        float scale = newWidth / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 获取本地图片大小
     *
     * @return
     */
    public static int[] getLocalImageSize(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
            // 防止个别手机获取的宽高是-1
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    LogUtils.i("exif height: " + height);
                    LogUtils.i("exif width: " + width);
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new int[]{options.outWidth, options.outHeight};

        } else {
            return null;
        }
    }

    public static ImageSize getImageSize(int outWidth, int outHeight) {
        if (outWidth <= 0 || outHeight <= 0) {
            return null;
        }
        ImageSize imageSize = new ImageSize();
        int maxWidth = 170;
        int maxHeight = 170;
        int minWidth = 110;
        int minHeight = 110;
        if (outWidth / maxWidth > outHeight / maxHeight) {
            if (outWidth >= maxWidth) {
                imageSize.setWidth(maxWidth);
                imageSize.setHeight(outHeight * maxWidth / outWidth);
            } else {
                imageSize.setWidth(outWidth);
                imageSize.setHeight(outHeight);
            }
            if (outHeight < minHeight) {
                imageSize.setHeight(minHeight);
                int width = outWidth * minHeight / outHeight;
                if (width > maxWidth) {
                    imageSize.setWidth(maxWidth);
                } else {
                    imageSize.setWidth(width);
                }
            }
        } else {
            if (outHeight >= maxHeight) {
                imageSize.setHeight(maxHeight);
                imageSize.setWidth(outWidth * maxHeight / outHeight);
            } else {
                imageSize.setHeight(outHeight);
                imageSize.setWidth(outWidth);
            }
            if (outWidth < minWidth) {
                imageSize.setWidth(minWidth);
                int height = outHeight * minWidth / outWidth;
                if (height > maxHeight) {
                    imageSize.setHeight(maxHeight);
                } else {
                    imageSize.setHeight(height);
                }
            }
        }
        if (imageSize.getWidth() < 80) {
            imageSize.setWidth(80);
        }
        return imageSize;
    }

    /**
     * Return a Drawable form SD card.
     *
     * @param filePath
     * @return
     */
    public static Drawable getDrawableFromSDCard(String filePath) {
        File file = new File(filePath);
        Drawable drawable = null;
        if (file.exists()) {
            drawable = BitmapDrawable.createFromPath(filePath);
        }
        return drawable;
    }

    /**
     * Return a Bitmap from SD card.
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromSDCard(String filePath) {
        File file = new File(filePath);
        Bitmap bm = null;
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                bm = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return bm;
    }

    /**
     * Return a Bitmap from the assets directory.
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapFromAssets(Context context, String fileName) {
        Bitmap bm;
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            bm = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return bm;
    }

    /**
     * Return a Drawable from the assets directory.
     *
     * @param context
     * @param fileName
     * @return Drawable.
     */
    public static Drawable getDrawableFromAssets(Context context, String fileName) {
        Drawable drawable;
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            drawable = Drawable.createFromStream(is, "");
            is.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return drawable;
    }

    /**
     * Return a Bitmap with round corner.
     *
     * @param mContext
     * @param resId
     * @param roundPixels
     * @return
     */
    public static Bitmap getRoundCornerImage(Context mContext, int resId, int roundPixels) {
        Bitmap bitmap;
        try {
            bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(resId)).getBitmap();
            return getRoundCornerImage(bitmap, roundPixels);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return a Bitmap with round corner.
     *
     * @param bitmap
     * @param roundPixels
     * @return
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels) {
        Bitmap roundConcerImage;
        Canvas canvas;
        Paint paint;
        Rect rect;
        RectF rectF;
        try {
            roundConcerImage =
                    Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(roundConcerImage);
            paint = new Paint();
            paint.setColor(0xFFf1d9c3);
            rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(bitmap, null, rect, paint);
            return roundConcerImage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Combine two bitmaps to a bitmap.
     *
     * @param background
     * @param foreground
     * @return
     */
    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        Bitmap result;
        try {
            if (background == null) {
                return null;
            }
            int bgWidth = background.getWidth();
            int bgHeight = background.getHeight();
            int fgWidth = foreground.getWidth();
            int fgHeight = foreground.getHeight();
            result = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(result);
            cv.drawBitmap(background, 0, 0, null);
            cv.drawBitmap(foreground, (bgWidth - fgWidth) / 2, (bgHeight - fgHeight) / 2, null);
            cv.save(Canvas.ALL_SAVE_FLAG);
            // store
            cv.restore();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            return null;
        }
        int degree = getBitmapDegree(path);
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Uri parse(String imageUrl) {
        Uri result;
        if (imageUrl.startsWith("http")) {
            result = Uri.parse(imageUrl);
        } else {
            result = Uri.parse("file://" + imageUrl);
        }
        return result;
    }

    /**
     * 检测系统是否有相机应用
     *
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return (null != list && list.size() > 0);
    }

    public static Bitmap getLocalBitmap(String localImagePath) {
        if (!TextUtils.isEmpty(localImagePath)) {
            File imageFile = new File(localImagePath);
            if (imageFile.exists() && imageFile.isFile()) {
                Bitmap bitmap = decodeBitmapFromFile(localImagePath, SizeUtils.getScreenWidth(CoreLib.getContext()) / 3,
                        SizeUtils.getScreenHeight(CoreLib.getContext()) / 2);
                if (bitmap != null) {
                    return bitmap;
                }
            }
        }
        return null;
    }

    /**
     * 压缩Bitmap的大小
     *
     * @param imagePath     图片文件路径
     * @param requestWidth  压缩到想要的宽度
     * @param requestHeight 压缩到想要的高度
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (requestWidth <= 0 || requestHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                return bitmap;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
            // 防止个别手机获取的宽高是-1
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    LogUtils.i("exif height: " + height);
                    LogUtils.i("exif width: " + width);
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(imagePath, options);
        } else {
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        LogUtils.i("height: " + height);
        LogUtils.i("width: " + width);
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    public static boolean isLocalFileExist(String localImagePath) {
        if (!TextUtils.isEmpty(localImagePath)) {
            File imageFile = new File(localImagePath);
            if (imageFile.exists() && imageFile.isFile()) {
                return true;
            }
        }
        return false;
    }

    public static String saveTempBitmap(Bitmap bitmap, String imageName) {
        String path = CoreLib.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + imageName + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*将像框和图片进行融合，返回一个Bitmap*/
    public static Bitmap montageBitmap(Bitmap frame, Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap sizeFrame = Bitmap.createScaledBitmap(frame, w, h, true);

        Bitmap newBM = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBM);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(sizeFrame, 100, 100, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
        return newBM;
    }

    /**
     * 保存View为图片的方法
     */
    public static String saveViewToBitmap(View v, String name) {
        String fileName = name + ".png";
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        return saveBitmap(bm, fileName, true);
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imageName
     */
    public static String saveBitmap(Bitmap bitmap, String imageName, boolean isShowTip) {
        return saveBitmap(bitmap, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), imageName, isShowTip);
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imageName
     */
    public static String saveBitmap(Bitmap bitmap, String pathStr, String imageName, boolean isShowTip) {
        File path = new File(pathStr);
        FileOutputStream fos = null;
        File targetFile = new File(path, imageName);
        if (bitmap == null && targetFile.exists()) {
            return targetFile.getAbsolutePath();
        }
        try {
            if (bitmap != null) {
                fos = new FileOutputStream(new File(path, imageName));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                // 最后通知图库更新
                CoreLib.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(path.getPath()))));
                if (isShowTip) {
                    Toast.makeText(CoreLib.getContext(), "图片已保存到" + targetFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
                return targetFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据路径获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFileNameByFilePath(String filePath) {
        String result;
        int lastIndex = filePath.lastIndexOf("/");
        if (lastIndex > 0) {
            result = filePath.substring(lastIndex + 1);
        } else {
            result = System.currentTimeMillis() + ".jpg";
        }
        return result;
    }

    /**
     * 根據返回的图片格式获取图片id List
     *
     * @param imageStr
     * @return
     */
    public static List<String> getImageIdList(String imageStr) {
        List<String> resultList = new ArrayList<>();
        if (!TextUtils.isEmpty(imageStr)) {
            imageStr = imageStr.replaceAll("\\[", "");
            imageStr = imageStr.replaceAll("]", "");
            StringBuilder stringBuilder = new StringBuilder("[");
            stringBuilder.append(imageStr).append("]");
            LogUtils.i("要解析的格式：" + stringBuilder.toString());
            try {
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Object object = jsonArray.get(i);
                        LogUtils.i("解析图片结果是：" + object);
                        if (object instanceof String) {
                            String imageId = (String) object;
                            if (!TextUtils.isEmpty(imageId)) {
                                resultList.add(imageId);
                            }
                        } else if (object instanceof Long) {
                            Long jsonObject = (Long) object;
                            if (jsonObject > 0) {
                                if (!TextUtils.isEmpty(String.valueOf(jsonObject))) {
                                    resultList.add(String.valueOf(jsonObject));
                                }
                            }
                        } else if (object instanceof JSONObject) {
                            JSONObject jsonObject = (JSONObject) object;
                            String imageId = jsonObject.getString("id");
                            if (!TextUtils.isEmpty(imageId)) {
                                resultList.add(imageId);
                            }
                        }
                    }
                }
                int length = jsonArray.length();
                LogUtils.i("解析图片结果长度是：" + length);

            } catch (JSONException e) {
                LogUtils.i("解析图片错误");
                e.printStackTrace();
            }
        }
        return resultList;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                           int kind) {
        File file = new File(videoPath);
        if (!file.exists()) {
            LogUtils.i("视频不存在：" + videoPath);
            return null;
        }
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap == null) {
            return null;
        }
//        LogUtils.i("cme", "视频缩略图宽：" + bitmap.getWidth() + "  高：" + bitmap.getHeight());
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * Convert a byte[] to a Bitmap.
     *
     * @param b
     * @return
     */
    public Bitmap bytesToBitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
