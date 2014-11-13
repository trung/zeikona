package org.mdkt.zeikona.model;

/**
 * Created by trung on 29/9/14.
 */
public class ZUserDTO {
    private String userId;
    private String displayName;
    private String publicProfileUrl;
    private String publicProfilePhotoUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPublicProfileUrl() {
        return publicProfileUrl;
    }

    public void setPublicProfileUrl(String publicProfileUrl) {
        this.publicProfileUrl = publicProfileUrl;
    }

    public String getPublicProfilePhotoUrl() {
        return publicProfilePhotoUrl;
    }

    public void setPublicProfilePhotoUrl(String publicProfilePhotoUrl) {
        this.publicProfilePhotoUrl = publicProfilePhotoUrl;
    }
}
