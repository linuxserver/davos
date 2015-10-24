package io.linuxserver.davos.schedule.workflow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PushbulletNotifyAction implements PostDownloadAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushbulletNotifyAction.class);
    private RestTemplate restTemplate = new RestTemplate();
    private String apiKey;

    public PushbulletNotifyAction(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void execute(PostDownloadExecution execution) {

        PushbulletRequest body = new PushbulletRequest();
        body.body = execution.fileName;
        body.title = "A new file has been downloaded";
        body.type = "note";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + apiKey);

        try {

            LOGGER.info("Sending notification to Pushbullet for {}", execution.fileName);
            HttpEntity<PushbulletRequest> httpEntity = new HttpEntity<PushbulletRequest>(body, headers);
            LOGGER.debug("Sending message to Pushbullet: {}", httpEntity);
            restTemplate.exchange("https://api.pushbullet.com/v2/pushes", HttpMethod.POST, httpEntity, Object.class);

        } catch (RestClientException | HttpMessageConversionException e ) {
            
            LOGGER.debug("Full stacktrace", e);
            LOGGER.error("Unable to complete notification to Pushbullet. Given error: {}", e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    class PushbulletRequest {

        public String type;
        public String title;
        public String body;

        @Override
        public String toString() {
            return "PushbulletRequest [type=" + type + ", title=" + title + ", body=" + body + "]";
        }
    }
}
