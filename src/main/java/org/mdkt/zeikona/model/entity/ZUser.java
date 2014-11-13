package org.mdkt.zeikona.model.entity;

import com.googlecode.objectify.annotation.Entity;

/**
 * Created by trung on 13/11/14.
 */
@Entity
public class ZUser {
    private String userId;
    private String csrfToken;
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
