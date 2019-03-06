package com.vector.update_app.update;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/7/26.
 * 版本信息bean
 */

public class VersionBean {

    /**
     * state : 1
     * message : null
     * data : {"id":"1","versionCode":1006,"versionName":"1","apkSize":"1","updateInfo":"1","isForceUpdate":"1","title":"123","url":"http://cir.cmeplaza.com:8866/download/android/app-release-cme.apk","updateTime":1500971006000,"appCode":null}
     * code : null
     */

    private int state;
    private Object message;
    private DataBean data;
    private Object code;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * id : 1
         * versionCode : 1006
         * versionName : 1
         * apkSize : 1
         * updateInfo : 1
         * isForceUpdate : 1
         * title : 123
         * url : http://cir.cmeplaza.com:8866/download/android/app-release-cme.apk
         * updateTime : 1500971006000
         * appCode : null
         */

        private String id;
        private int versionCode;
        private String versionName;
        private String apkSize;
        private String updateInfo;
        private String isForceUpdate;
        private String title;
        private String url;
        private long updateTime;
        private Object appCode;
        private String md5;

        public String getMd5() {
            return TextUtils.isEmpty(md5) ? "" : md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getApkSize() {
            return apkSize;
        }

        public void setApkSize(String apkSize) {
            this.apkSize = apkSize;
        }

        public String getUpdateInfo() {
            return updateInfo;
        }

        public void setUpdateInfo(String updateInfo) {
            this.updateInfo = updateInfo;
        }

        public boolean getIsForceUpdate() {
            return TextUtils.equals("1", isForceUpdate);
        }

        public void setIsForceUpdate(String isForceUpdate) {
            this.isForceUpdate = isForceUpdate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public Object getAppCode() {
            return appCode;
        }

        public void setAppCode(Object appCode) {
            this.appCode = appCode;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", versionCode=" + versionCode +
                    ", versionName='" + versionName + '\'' +
                    ", apkSize='" + apkSize + '\'' +
                    ", updateInfo='" + updateInfo + '\'' +
                    ", isForceUpdate='" + isForceUpdate + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", updateTime=" + updateTime +
                    ", appCode=" + appCode +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VersionBean{" +
                "state=" + state +
                ", message=" + message +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
