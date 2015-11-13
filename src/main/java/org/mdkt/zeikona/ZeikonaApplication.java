package org.mdkt.zeikona;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.mdkt.zeikona.api.Constants;
import org.mdkt.zeikona.autoconfigure.ZeikonaProperties;
import org.mdkt.zeikona.dao.ZAlbumDao;
import org.mdkt.zeikona.dao.ZPhotoHashDao;
import org.mdkt.zeikona.model.ZAlbum;
import org.mdkt.zeikona.model.ZPhoto;
import org.mdkt.zeikona.model.ZPhotoHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoPhotosUsed;
import com.google.gdata.data.photos.GphotoTimestamp;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.ServiceException;

@SpringBootApplication
@EnableConfigurationProperties
public class ZeikonaApplication {
	private static final Logger logger = LoggerFactory.getLogger(ZeikonaApplication.class);

	@Autowired
	private ZeikonaProperties properties;

	@Autowired
	private ZAlbumDao albumDao;

	@Autowired
	private ZPhotoHashDao photoHashDao;

	private PicasawebService service;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ZeikonaApplication.class, args);
		try {
			context.getBean(ZeikonaApplication.class).connect();
		} catch (Exception e) {
			logger.error("Unable to connect", e);
			context.close();
		}
	}

	public void connect() throws Exception {
		Credential credential = authorize();
		service = new PicasawebService("zeikona-boot-application");
		service.setOAuth2Credentials(credential);
		GphotoEntry<?> autoBackupAlbum = loadAlbum("Auto Backup");
		Integer total = autoBackupAlbum.getExtension(GphotoPhotosUsed.class).getValue();
		logger.info("Total photos = {}", total);
		processAlbum(autoBackupAlbum);
	}

	private void processAlbum(GphotoEntry<?> autoBackupAlbum) throws MalformedURLException {
		ZAlbum album = new ZAlbum();
		album.setId(autoBackupAlbum.getGphotoId());
		album.setName(autoBackupAlbum.getTitle().getPlainText());
		album.setPhotoCount(autoBackupAlbum.getExtension(GphotoPhotosUsed.class).getValue());
		album = albumDao.save(album);
		logger.info("Saved album info. {}", album);
		String albumId = album.getId();
		int limit = properties.getBatch();
		int offset = 1;
		int count = 0;
		int processed = 0;
		do {
			String photoUrl = "https://picasaweb.google.com/data/feed/api/user/default/albumid/" + albumId + "?max-results=" + limit + "&start-index=" + offset;
			logger.info("Loading photos from URL {}", photoUrl);
			URL photoFeedsUrl = new URL(photoUrl);
			AlbumFeed albumFeed;
			try {
				albumFeed = service.getFeed(photoFeedsUrl, AlbumFeed.class);
				List<GphotoEntry> photos = albumFeed.getEntries();
				count = photos.size();
				for (GphotoEntry<?> ge : albumFeed.getEntries()) {
					PhotoEntry pe = new PhotoEntry(ge);
					MediaThumbnail mt = pe.getMediaThumbnails().get(pe.getMediaThumbnails().size() - 1);
					String hash = DigestUtils.md5Hex(pe.getSize() + pe.getTitle().getPlainText());
					ZPhoto zp = new ZPhoto(mt.getUrl());
					zp.setHeight(mt.getHeight());
					zp.setWidth(mt.getWidth());
					zp.setSize(pe.getSize());
					zp.setHash(hash);
					zp.setId(pe.getGphotoId());
					zp.setTimestamp(new Date(pe.getExtension(GphotoTimestamp.class).getValue()));
					zp.setPublishedTimestamp(new Date(pe.getPublished().getValue()));
					zp.setName(pe.getTitle().getPlainText());
					ZPhotoHash photoHash = new ZPhotoHash();
					photoHash.setHash(hash);
					photoHash.setPhoto(zp);
					photoHashDao.save(photoHash);
				}
				offset += count;
				processed += count;
			} catch (IOException | ServiceException e) {
				logger.error("Unable to process photos, offset = {}", offset, e);
				break;
			}
		} while (count > 0 || offset >= album.getPhotoCount());
		album.setPhotoProcessedCount(processed);
		logger.info("Processing done! {}", album);
	}

	private Credential authorize() throws Exception {
		String clientSecretJson = new ObjectMapper().writeValueAsString(properties.getOAuth2Credential());
		logger.info("Authenticating with Google Server using {} ...", clientSecretJson);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(Constants.JSON_FACTORY, new StringReader(clientSecretJson));
		List<String> scopes = Arrays.asList(Constants.PICASAWEB_SCOPE);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), Constants.JSON_FACTORY, clientSecrets, scopes).build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	private GphotoEntry<?> loadAlbum(String albumName) throws Exception {
		String albumUrl = "https://picasaweb.google.com/data/feed/api/user/default?kind=album&max-results=1&fields=entry(title,id,gphoto:*)";
		logger.info("Loading ablumns from {}", albumUrl);
		URL feedUrl = new URL(albumUrl);

		UserFeed myUserFeed = service.getFeed(feedUrl, UserFeed.class);

		for (GphotoEntry<?> myAlbum : myUserFeed.getEntries()) {
			logger.debug("Got {}", myAlbum.getTitle().getPlainText());
			if (albumName.equals(myAlbum.getTitle().getPlainText())) {
				Integer total = myAlbum.getExtension(GphotoPhotosUsed.class).getValue();
				logger.debug("{} albumn detected with id {}, total photos = {}", albumName, myAlbum.getGphotoId(), total);
				return myAlbum;
			}
		}

		throw new IllegalArgumentException("Album " + albumName + " does not exist");
	}
}
