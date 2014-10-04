package org.mdkt.zeikona.model;

/**
 * Created by trung on 3/10/14.
 */
public class ZPhoto {

    private String thumbnail;
    private int height;
    private int width;

    public ZPhoto(String thumbnailUrl) {
        this.thumbnail = thumbnailUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
