# 核心类库module
## 使用说明
1. 本module是通用类库，包含各种通用工具类，最好在Application中调用
    ~~~
    CoreLib.init(this);
    CoreLib.initNet(String baseUrl, String baseH5Url);
    ~~~
2. 因为module间页面跳转使用的阿里的ARouter框架，所以要在Application中
    尽可能早的初始化
    ~~~
    if (BuildConfig.DEV_MODE) {
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();  // debug模式
    }
    ARouter.init(this);
    ~~~
3. 图片加载使用的是根据第三方的图片加载库封装的工具类，所以要在Application
    中初始化一下，调用以下方法：
    ~~~
    ImageLoaderManager.getInstance().init(this);
    ~~~
4. 因为多个module中都要用到当前用户id，昵称，头像等信息，
    所有在获取到用户信息后调用以下方法，保存起来，以便各个模块的使用：
    ~~~
    CoreLib.setCurrentUserId(dataBean.getAccId());
    CoreLib.setCurrentUserName(dataBean.getNickName());
    CoreLib.setCurrentUserPhone(dataBean.getMobile());
    ~~~
5. 项目中各接口都使用到了session，来判断用户权限，在调用登录接口成功
    后要保存一下session,以便各个接口的使用，调用：
    ~~~
    CoreLib.setSession(String session);
    ~~~
    获取session调用:
    ~~~
    CoreLib.getSession();
    ~~~
6. 退出登录后要清空当前用户的本地缓存信息，调用以下方法：
    ~~~
    CoreLib.clearUserCache();
    ~~~