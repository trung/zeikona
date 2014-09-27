package org.mdkt.zeikona.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Created by trung on 27/9/14.
 */
@Api(
        name = "helloApi",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID}
)
public class HelloAPI {

    @ApiMethod
    public String hello(@Named("name") String name, User user) {
        return String.format("Hi, %s", user.getUserId());
    }
}
