package org.mdkt.zeikona.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import org.mdkt.zeikona.model.GPAuthResult;
import org.mdkt.zeikona.model.ZUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by trung on 27/9/14.
 */
@Api(
        name = "initialization",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID},
        description = "Used during initialization, established the log in process"
)
public class InitializationAPI {

    private static final Logger logger = LoggerFactory.getLogger(InitializationAPI.class);

    @ApiMethod(name = "connect")
    public ZUser connect(GPAuthResult authResult) throws UnauthorizedException {
        GoogleCredential googleCredential = new GoogleCredential.Builder()
                .setJsonFactory(Constants.JSON_FACTORY)
                .setTransport(Constants.TRANSPORT)
                .setClientSecrets(Constants.WEB_CLIENT_ID, Constants.CLIENT_SECRET)
                .build();
        if (authResult.getCode() != null && authResult.getCode().length() > 0) {
            try {
                logger.info("Obtain token response from authResult code");
                GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(Constants.TRANSPORT, Constants.JSON_FACTORY,
                        Constants.WEB_CLIENT_ID, Constants.CLIENT_SECRET,
                        authResult.getCode(), "postmessage")
                        .execute();
                googleCredential.setFromTokenResponse(tokenResponse);
            } catch (IOException e) {
                throw new UnauthorizedException("Unable to obtain token");
            }
        }

        googleCredential.setAccessToken(authResult.getAccess_token());
        googleCredential.setExpirationTimeMilliseconds(authResult.getExpires_at());
        googleCredential.setExpiresInSeconds(authResult.getExpires_in());

        Plus plus = new Plus.Builder(Constants.TRANSPORT, Constants.JSON_FACTORY, googleCredential).build();
        Person profile;
        Plus.People.Get get;
        try {
            get = plus.people().get("me");
            logger.info("Obtain user profile");
            profile = get.execute();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        ZUser user = new ZUser();
        user.setUserId(profile.getId());
        user.setDisplayName(profile.getDisplayName());
        user.setPublicProfileUrl(profile.getUrl());
        user.setPublicProfilePhotoUrl(profile.getImage().getUrl());

        return user;
    }
}