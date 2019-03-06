package com.cme.corelib.utils.image;

import com.cme.corelib.utils.GsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 * 图片bean
 */

public class ImageBean implements Serializable {

    private String code;
    private int state;
    private List<DataBean> data;

    public boolean isSuccess() {
        return 1 == state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 18099218593882112
         * originalName : 20170504072016.jpg
         */

        private String id;
        private String originalName;
        private String showSize;
        private String filesSize;
        private String fileType;
        private String showUnit;
        private int width;  // 图片宽度
        private int height; // 图片高度

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getShowSize() {
            return showSize;
        }

        public void setShowSize(String showSize) {
            this.showSize = showSize;
        }

        public String getFilesSize() {
            return filesSize;
        }

        public void setFilesSize(String filesSize) {
            this.filesSize = filesSize;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getShowUnit() {
            return showUnit;
        }

        public void setShowUnit(String showUnit) {
            this.showUnit = showUnit;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", originalName='" + originalName + '\'' +
                    ", showSize='" + showSize + '\'' +
                    ", filesSize='" + filesSize + '\'' +
                    ", fileType='" + fileType + '\'' +
                    ", showUnit='" + showUnit + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    public String getGson(DataBean location) {
        String json = GsonUtils.parseClassToJson(location);
        return json;
    }

    public String getGson(List<DataBean> location) {
        String json = GsonUtils.parseClassToJson(location);
        return json;
    }
}
