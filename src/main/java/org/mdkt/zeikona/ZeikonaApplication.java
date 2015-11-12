package org.mdkt.zeikona;

import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.mdkt.zeikona.api.Constants;
import org.mdkt.zeikona.autoconfigure.ZeikonaProperties;
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
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoPhotosUsed;
import com.google.gdata.data.photos.UserFeed;

@SpringBootApplication
@EnableConfigurationProperties
public class ZeikonaApplication {
	private static final Logger logger = LoggerFactory.getLogger(ZeikonaApplication.class);

	@Autowired
	private ZeikonaProperties properties;

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
		logger.info("Total photos = {}", autoBackupAlbum.getExtension(GphotoPhotosUsed.class).getValue());
	}

	private Credential authorize() throws Exception {
		String clientSecretJson = new ObjectMapper().writeValueAsString(properties.getOAuth2Credential());
		logger.info("Authenticating with Google Server using {} ...", clientSecretJson);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(Constants.JSON_FACTORY, new StringReader(clientSecretJson));
		List<String> scopes = Arrays.asList(
			Constants.PICASAWEB_SCOPE
		);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), Constants.JSON_FACTORY, clientSecrets, scopes)
			.build();
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
