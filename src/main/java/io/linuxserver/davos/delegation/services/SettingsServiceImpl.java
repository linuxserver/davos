package io.linuxserver.davos.delegation.services;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.linuxserver.davos.Version;
import io.linuxserver.davos.logging.LoggingManager;
import io.linuxserver.davos.web.selectors.LogLevelSelector;

@Component
public class SettingsServiceImpl implements SettingsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private LogLevelSelector currentLevel = LogLevelSelector.INFO;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setLoggingLevel(LogLevelSelector level) {
        currentLevel = level;
        LoggingManager.setLogLevel(Level.valueOf(level.toString()));
    }

    @Override
    public LogLevelSelector getCurrentLoggingLevel() {
        return currentLevel;
    }

    @Override
    public Version retrieveRemoteVersion() {

        try {

            String gitHubURL = "https://raw.githubusercontent.com/linuxserver/davos/LatestRelease/version.txt";

            LOGGER.debug("Calling out to GitHub to check for new version ({})", gitHubURL);
            ResponseEntity<String> response = restTemplate.exchange(gitHubURL, HttpMethod.GET,
                    new HttpEntity<String>(new HttpHeaders()), String.class);

            String body = response.getBody();
            LOGGER.debug("GitHub responded with a {}, and body of {}", response.getStatusCode(), body);
            return new Version(body);

        } catch (RestClientException | HttpMessageConversionException e) {
            
            LOGGER.error("Unable to get version from GitHub: {}", e.getMessage(), e);
            LOGGER.debug("Defaulting remote version to zero");
            return new Version(0, 0, 0);
        }
    }
}
