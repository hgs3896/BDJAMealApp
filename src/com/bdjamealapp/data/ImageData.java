package com.bdjamealapp.data;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13. 10. 13
 * Time: 오전 7:07
 * To change this template use File | Settings | File Templates.
 */
public class ImageData {
    public ImageData() {
    }

    public ImageData(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }

    public int width, height;
    public String url;
}
