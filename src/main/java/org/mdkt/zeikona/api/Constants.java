package org.mdkt.zeikona.api;


import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * Created by trung on 27/9/14.
 */
public class Constants {
    public static final String WEB_CLIENT_ID = "13487503673-h0ejrqftar980f4vkk3o76q0l6mu69at.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "gJl8dHAILpcuH3ozqlgvar8L";

    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final HttpTransport TRANSPORT = new NetHttpTransport();
    public static final String PLUS_SCOPE = "https://www.googleapis.com/auth/plus.login";
    public static final String PICASAWEB_SCOPE = "https://picasaweb.google.com/data/";
    public static final String BEARER = "Bearer ";
}
