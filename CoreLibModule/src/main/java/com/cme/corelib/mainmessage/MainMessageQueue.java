package com.cme.corelib.mainmessage;

import java.util.LinkedList;

/**
 * Created by klx on 2018/3/20.
 * 首页的消息队列
 */

public class MainMessageQueue {
    private LinkedList<Object> list = new LinkedList();

    //销毁队列
    public void clear() {
        list.clear();
    }

    //判断队列是否为空
    public boolean isEmpty() {
        return list.isEmpty();
    }

    //进队
    public void offer(Object o) {
        list.addLast(o);
    }

    //出队
    public Object poll() {
        if (!list.isEmpty()) {
            return list.removeFirst();
        }
        return null;
    }

    //获取队列长度
    public int queueLength() {
        return list.size();
    }

    //查看队首元素
    public Object getFirst() {
        return list.getFirst();
    }
}
