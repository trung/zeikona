package org.mdkt.zeikona.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 3/10/14.
 */
public class ZPhotos {

    private String albumId;

    private long total;
    private long processedCount;

    private List<ZPhoto> photos = new ArrayList<ZPhoto>();

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public List<ZPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ZPhoto> photos) {
        this.photos = photos;
    }

    public void add(ZPhoto zPhoto) {
        photos.add(zPhoto);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(long processedCount) {
        this.processedCount = processedCount;
    }
}
