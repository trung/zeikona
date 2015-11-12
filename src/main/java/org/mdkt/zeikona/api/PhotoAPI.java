package org.mdkt.zeikona.api;



public class PhotoAPI { }
/**
 * Created by trung on 1/10/14.
 *
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
    private Map<String, List<ZPhoto>> sizeHash = new HashMap<>();


    @ApiMethod(name = "photo.allPhotos")
    public ZPhotos allPhotos(@Named("offset") int offset, @Named("limit") int limit, @Nullable @Named("albumId") String albumId, User user, HttpServletRequest httpServletRequest) throws UnauthorizedException, OAuthRequestException {
        if (user == null) {
            throw new UnauthorizedException("User has not authenticated");
        }
        String accessToken = getAccessToken(httpServletRequest);
        logger.info("User authDomain={}, email={}, fId={}, userId={}, nickName={}, consumerKey={}", user.getAuthDomain(), user.getEmail(), user.getFederatedIdentity(), user.getUserId(), user.getNickname(), OAuthServiceFactory.getOAuthService().getOAuthConsumerKey());

        ZPhotos photos = new ZPhotos();
        try {
            PicasawebService myService = new PicasawebService("zeikona");
            myService.setHeader("GData-Version", "2");
            myService.setConnectTimeout(60000);
            myService.setReadTimeout(60000);
            myService.setOAuth2Credentials(new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken));

            if (albumId == null || albumId.length() == 0) {
                albumId = loadAlbum(myService, "Auto Backup", photos);
            }

            loadPhotos(myService, photos, albumId, offset, limit, sizeHash);
            for (String photoHash : sizeHash.keySet()) {
                if (sizeHash.get(photoHash).size() > 1) {
                    for (ZPhoto zp : sizeHash.get(photoHash)) {
                        photos.add(zp);
                    }
                }
            }

        } catch (UnauthorizedException ue) {
            throw ue;
        } catch (Exception e) {
            throw new RuntimeException("Unable to get photos", e);
        }
        return photos;
    }

    private String loadAlbum(PicasawebService myService, String albumTitle, ZPhotos photos) throws UnauthorizedException, IOException, ServiceException {
        String albumUrl = "https://picasaweb.google.com/data/feed/api/user/default?kind=album&max-results=1&fields=entry(title,id,gphoto:*)";
        logger.info("Loading ablumns {}", albumUrl);
        URL feedUrl = new URL(albumUrl);

        UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);

        for (GphotoEntry myAlbum : myUserFeed.getEntries()) {
            if (albumTitle.equals(myAlbum.getTitle().getPlainText())) {
                Integer total = myAlbum.getExtension(GphotoPhotosUsed.class).getValue();
                logger.debug("Auto Backup albumn detected with id {}, total photos = {}", myAlbum.getId(), total);
                photos.setTotal(total);
                // i just need the auto backup albumn
                return myAlbum.getGphotoId();
            }
        }

        throw new UnauthorizedException("Not allowed to get photos from Auto Backup album or album does not exist");
    }

    private int loadPhotos(PicasawebService myService, ZPhotos photos, String albumId, int offset, int limit, Map<String, List<ZPhoto>> sizeHash) throws IOException, ServiceException {
        String photoUrl = "https://picasaweb.google.com/data/feed/api/user/default/albumid/" + albumId + "?max-results=" + limit + "&start-index=" + offset;
        photos.setAlbumId(albumId);
        logger.info("Loading photos {}", photoUrl);
        URL photoFeedsUrl = new URL(photoUrl);
        AlbumFeed albumFeed = myService.getFeed(photoFeedsUrl, AlbumFeed.class);
        // Integer totalCount = albumFeed.getExtension(GphotoPhotosUsed.class).getValue();
        // logger.info("Total photos: {}", totalCount);
        for (GphotoEntry ge : albumFeed.getEntries()) {
            PhotoEntry pe = new PhotoEntry(ge);
            MediaThumbnail mt = pe.getMediaThumbnails().get(pe.getMediaThumbnails().size() - 1);
            String photoHash = org.mortbay.jetty.security.Credential.MD5.digest(String.valueOf(pe.getSize())) + org.mortbay.jetty.security.Credential.MD5.digest(pe.getTitle().getPlainText());
            ZPhoto zp = new ZPhoto(mt.getUrl());
            zp.setHeight(mt.getHeight());
            zp.setWidth(mt.getWidth());
            zp.setSize(pe.getSize());
            zp.setHash(photoHash);
            zp.setId(pe.getGphotoId());
            zp.setTimestamp(new Date(pe.getExtension(GphotoTimestamp.class).getValue()));
            zp.setPublishedTimestamp(new Date(pe.getPublished().getValue()));
            zp.setName(pe.getTitle().getPlainText());
            List<ZPhoto> current = sizeHash.get(photoHash);
            if (current == null) {
                current = new ArrayList<>();
                sizeHash.put(photoHash, current);
            }
            current.add(zp);
        }
        return albumFeed.getEntries().size();
    }

    private String getAccessToken(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        String authorizationValue = httpServletRequest.getHeader("Authorization");
        if (authorizationValue != null && authorizationValue.startsWith(Constants.BEARER)) {
            return authorizationValue.replaceFirst(Constants.BEARER, "");
        }
        throw new UnauthorizedException("Unable to obtain access token");
    }
}
*/
