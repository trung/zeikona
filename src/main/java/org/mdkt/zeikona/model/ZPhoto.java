package org.mdkt.zeikona.model;

import java.util.Date;

/**
 * Created by trung on 3/10/14.
 */
public class ZPhoto {

    private String thumbnail;
    private int height;
    private int width;
    private long size;
    private String hash;
    private String name;
    private Date timestamp;
    private Date publishedTimestamp;
    private String id;

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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getPublishedTimestamp() {
        return publishedTimestamp;
    }

    public void setPublishedTimestamp(Date publishedTimestamp) {
        this.publishedTimestamp = publishedTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
