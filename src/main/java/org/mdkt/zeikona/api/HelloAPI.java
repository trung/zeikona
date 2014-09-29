package org.mdkt.zeikona.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.mdkt.zeikona.model.GPAuthResult;
import org.mdkt.zeikona.model.ZUser;

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

    @ApiMethod(name = "say")
    public ZUser hello(GPAuthResult authResult) {
        ZUser user = new ZUser();
        user.setName(authResult.getAccess_token());
        return user;
    }
}
