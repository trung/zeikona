package org.mdkt.zeikona.api;


import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Constants {
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final String PICASAWEB_SCOPE = "https://picasaweb.google.com/data/";
    public static final String BEARER = "Bearer ";
}
