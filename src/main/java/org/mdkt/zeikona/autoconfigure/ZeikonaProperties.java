package org.mdkt.zeikona.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zeikona")
public class ZeikonaProperties {
	private GoogleOAuth2Credential OAuth2Credential;
	private int batch = 1000;

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}

	public GoogleOAuth2Credential getOAuth2Credential() {
		return OAuth2Credential;
	}

	public void setOAuth2Credential(GoogleOAuth2Credential oAuth2Credential) {
		OAuth2Credential = oAuth2Credential;
	}

	public static class GoogleOAuth2Credential {
		private Installed installed;

		public Installed getInstalled() {
			return installed;
		}

		public void setInstalled(Installed installed) {
			this.installed = installed;
		}

		public static class Installed {
			private String client_id;
			private String client_secret;

			public String getClient_id() {
				return client_id;
			}

			public void setClient_id(String client_id) {
				this.client_id = client_id;
			}

			public String getClient_secret() {
				return client_secret;
			}

			public void setClient_secret(String client_secret) {
				this.client_secret = client_secret;
			}
		}
	}
}
