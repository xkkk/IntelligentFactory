package com.cmeplaza.intelligentfactory.login.bean;

/**
 * Created by klx on 2018/5/11.
 * 个人信息
 */

public class PersonalInfoBean {

    /**
     * state : 0
     * message : 请求成功
     * data : {"card":"证件号","createTime":"","deleted":0,"invitationCode":"我的邀请码","leaderUserId":"我的上级用户id","level":1,"money":1000,"niceName":"我的昵称","phone":"我的手机号","robot":"我的机器人总数","smartManId":"我的智能人id","subordinate":10,"trueName":"我的真实名称","updateTime":"更新时间","userId":"用户id"}
     * code : 0000
     */

    private int state;
    private String message;
    private DataBean data;
    private String code;

    public boolean isSuccess() {
        return 1 == state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * card : 证件号
         * createTime :
         * deleted : 0
         * invitationCode : 我的邀请码
         * leaderUserId : 我的上级用户id
         * level : 1.0
         * money : 1000.0
         * niceName : 我的昵称
         * phone : 我的手机号
         * robot : 我的机器人总数
         * smartManId : 我的智能人id
         * subordinate : 10
         * trueName : 我的真实名称
         * updateTime : 更新时间
         * userId : 用户id
         */

        private String card;
        private String createTime;
        private int deleted;
        private String invitationCode;
        private String leaderUserId;
        private double level;
        private double money;
        private String niceName;
        private String phone;
        private String robot;
        private String smartManId;
        private int subordinate;
        private String trueName;
        private String updateTime;
        private int isComplete;  //是否完善用户信息 1 需要完善  0--不需要完善
        private String userId;

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getInvitationCode() {
            return invitationCode;
        }

        public void setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
        }

        public String getLeaderUserId() {
            return leaderUserId;
        }

        public void setLeaderUserId(String leaderUserId) {
            this.leaderUserId = leaderUserId;
        }

        public double getLevel() {
            return level;
        }

        public void setLevel(double level) {
            this.level = level;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getNiceName() {
            return niceName;
        }

        public void setNiceName(String niceName) {
            this.niceName = niceName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRobot() {
            return robot;
        }

        public void setRobot(String robot) {
            this.robot = robot;
        }

        public String getSmartManId() {
            return smartManId;
        }

        public void setSmartManId(String smartManId) {
            this.smartManId = smartManId;
        }

        public int getSubordinate() {
            return subordinate;
        }

        public void setSubordinate(int subordinate) {
            this.subordinate = subordinate;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        /**
         * 是否需要完善用户信息
         *
         * @return
         */
        public boolean isNeedCompleteUserInfo() {
            return 1 == isComplete;
        }

        public void setCompleteUserInfo() {
            isComplete = 0;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
