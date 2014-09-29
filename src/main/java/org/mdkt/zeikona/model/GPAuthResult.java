package org.mdkt.zeikona.model;

/**
 * This is to replicate authResult passed in the callback method when calling gapi.signin.render()
 * Created by trung on 29/9/14.
 */
public class GPAuthResult {
    private String id_token;
    private String access_token;
    private int expires_in;
    private String error;

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
