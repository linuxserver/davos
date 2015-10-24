package io.linuxserver.davos.schedule.workflow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class HttpAPICallAction implements PostDownloadAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAPICallAction.class);

    private RestTemplate restTemplate = new RestTemplate();

    private String url;
    private HttpMethod method;
    private String contentType;
    private String body;

    public HttpAPICallAction(String url, HttpMethod method, String contentType, String body) {

        this.url = url;
        this.method = method;
        this.contentType = contentType;
        this.body = body;
    }

    @Override
    public void execute(PostDownloadExecution execution) {

        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", contentType);

            LOGGER.info("Sending message to generic API for {}", execution.fileName);
            HttpEntity<String> httpEntity = new HttpEntity<String>(body.replaceAll("\\$filename", execution.fileName), headers);
            LOGGER.debug("Sending message {} to generic API: {}", httpEntity, url);
            restTemplate.exchange(url, method, httpEntity, Object.class);
            
        } catch (RestClientException | HttpMessageConversionException e) {

            LOGGER.debug("Full stacktrace", e);
            LOGGER.error("Unable to complete message to generic API. Given error: {}", e.getMessage());
        }
    }
}
