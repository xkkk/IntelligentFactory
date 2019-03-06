package com.cme.corelib.utils;

import java.io.Serializable;

/**
 * Created by klx on 2017/11/7.
 */

public class ImageSize implements Serializable{
    private int position;
    private int width;
    private int height;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

    @Override
    public String toString() {
        return "ImageSize{" +
                "position=" + position +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
