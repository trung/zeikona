package org.mdkt.zeikona.api;


import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * Created by trung on 27/9/14.
 */
public class Constants {
    public static final String WEB_CLIENT_ID = "13487503673-mgiqme47e1uf23upgnqo29u1tif49up1.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "wXTTYS8eZZiehmAGxgcGInSV";

    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final HttpTransport TRANSPORT = new NetHttpTransport();
}
