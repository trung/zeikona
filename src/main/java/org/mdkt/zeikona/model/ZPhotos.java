package org.mdkt.zeikona.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 3/10/14.
 */
public class ZPhotos {

    private String albumId;

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
}
