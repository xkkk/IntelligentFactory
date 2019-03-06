package com.cme.corelib.mainmessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klx on 2018/3/20.
 * 首页消息的管理类
 */

public class MainQueueDataManager {
    private static MainQueueDataManager instance;

    private List<MainQueueDataWatcher> dataWatcherList;

    private MainQueueDataManager() {
        dataWatcherList = new ArrayList<>();
    }

    public static MainQueueDataManager getInstance() {
        if (instance == null) {
            synchronized (MainQueueDataManager.class) {
                if (instance == null) {
                    instance = new MainQueueDataManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加观察者
     *
     * @param watcher
     */
    public void addObserver(MainQueueDataWatcher watcher) {
        dataWatcherList.add(watcher);
    }

    /**
     * 移除观察者
     */
    public void deleteObserver(MainQueueDataWatcher watcher) {
        if (watcher == null) {
            return;
        }
        int index = dataWatcherList.indexOf(watcher);
        if (index >= 0) {
            if (watcher.getHandler() != null) {
                watcher.getHandler().removeCallbacksAndMessages(null);
            }
            dataWatcherList.remove(index);
        }
    }

    public void notifyDataChange(MainMessageQueue messageQueue) {
        if (messageQueue == null) {
            return;
        }
        if (dataWatcherList.size() > 0) {
            for (MainQueueDataWatcher mainQueueDataWatcher : dataWatcherList) {
                mainQueueDataWatcher.update(null, messageQueue.getFirst());
            }
        }
    }
}
