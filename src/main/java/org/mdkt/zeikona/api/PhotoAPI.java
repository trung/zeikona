package org.mdkt.zeikona.api;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import org.mdkt.zeikona.model.ZPhoto;
import org.mdkt.zeikona.model.ZPhotos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * Created by trung on 1/10/14.
 */
@Api(
        name = "zeikona",
        version = "v1",
        scopes = {Constants.PLUS_SCOPE, Constants.PICASAWEB_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.WEB_CLIENT_ID},
        description = "Photo-related operations"
)
public class PhotoAPI {
    private static final Logger logger = LoggerFactory.getLogger(PhotoAPI.class);

    @ApiMethod(name = "photo.allPhotos")
    public ZPhotos allPhotos(User user, HttpServletRequest httpServletRequest) throws UnauthorizedException, OAuthRequestException {
        if (user == null) {
            throw new UnauthorizedException("User has not authenticated");
        }
        String accessToken = getAccessToken(httpServletRequest);
        logger.info("User authDomain={}, email={}, fId={}, userId={}, nickName={}, consumerKey={}", user.getAuthDomain(), user.getEmail(), user.getFederatedIdentity(), user.getUserId(), user.getNickname(), OAuthServiceFactory.getOAuthService().getOAuthConsumerKey());

        ZPhotos photos = new ZPhotos();
        try {
            PicasawebService myService = new PicasawebService("zeikona");

            myService.setConnectTimeout(60000);
            myService.setReadTimeout(60000);
            myService.setOAuth2Credentials(new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken));

            String albumUrl = "https://picasaweb.google.com/data/feed/api/user/default?kind=album&max-results=1&fields=entry(title,id,gphoto:*)";
            logger.info("Loading ablumns {}", albumUrl);
            URL feedUrl = new URL(albumUrl);

            UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);

            for (GphotoEntry myAlbum : myUserFeed.getEntries()) {
                if ("Auto Backup".equals(myAlbum.getTitle().getPlainText())) {
                    logger.debug("Auto Backup albumn detected with id {}", myAlbum.getId());
                    // i just need the auto backup albumn
                    String photoUrl = "https://picasaweb.google.com/data/feed/api/user/default/albumid/" + myAlbum.getGphotoId() + "?max-results=20";
                    logger.info("Loading photos {}", photoUrl);
                    URL photoFeedsUrl = new URL(photoUrl);
                    AlbumFeed albumFeed = myService.getFeed(photoFeedsUrl, AlbumFeed.class);
                    for (GphotoEntry ge : albumFeed.getEntries()) {
                        PhotoEntry pe = new PhotoEntry(ge);
                        MediaThumbnail mt = pe.getMediaThumbnails().get(pe.getMediaThumbnails().size() - 1);
                        ZPhoto zp = new ZPhoto(mt.getUrl());
                        zp.setHeight(mt.getHeight());
                        zp.setWidth(mt.getWidth());
                        photos.add(zp);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to get photos", e);
        }
        return photos;
    }

    private String getAccessToken(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        String authorizationValue = httpServletRequest.getHeader("Authorization");
        if (authorizationValue != null && authorizationValue.startsWith(Constants.BEARER)) {
            return authorizationValue.replaceFirst(Constants.BEARER, "");
        }
        throw new UnauthorizedException("Unable to obtain access token");
    }
}
