package io.linuxserver.davos.schedule.workflow.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.linuxserver.davos.schedule.workflow.actions.PushbulletNotifyAction.PushbulletRequest;

public class PushbulletNotifyActionTest {

    @InjectMocks
    private PushbulletNotifyAction pushbulletNotifyAction;

    @Mock
    private RestTemplate mockRestTemplate;

    @Captor
    private ArgumentCaptor<HttpEntity<PushbulletRequest>> entityCaptor;

    @Before
    public void setUp() {

        pushbulletNotifyAction = new PushbulletNotifyAction("apiKey");

        initMocks(this);
    }
    
    @Test
    public void executeShouldSendCorrectData() {

        PostDownloadExecution execution = new PostDownloadExecution();
        execution.fileName = "filename";

        pushbulletNotifyAction.execute(execution);

        verify(mockRestTemplate).exchange(eq("https://api.pushbullet.com/v2/pushes"), eq(HttpMethod.POST), entityCaptor.capture(),
                eq(Object.class));

        PushbulletRequest request = entityCaptor.getValue().getBody();

        assertThat(request.type).isEqualTo("note");
        assertThat(request.title).isEqualTo("A new file has been downloaded");
        assertThat(request.body).isEqualTo("filename");
    }

    @Test
    public void postDataShouldHaveCorrectHeaderValue() {
        
        PostDownloadExecution execution = new PostDownloadExecution();
        execution.fileName = "filename";

        pushbulletNotifyAction.execute(execution);

        verify(mockRestTemplate).exchange(eq("https://api.pushbullet.com/v2/pushes"), eq(HttpMethod.POST), entityCaptor.capture(),
                eq(Object.class));

        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        
        assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(headers.get("Authorization").get(0)).isEqualTo("Bearer apiKey");
    }
    
    @Test
    public void ifRestTemplateFailsThenDoNothing() {
        
        when(mockRestTemplate.exchange(eq("https://api.pushbullet.com/v2/pushes"), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(Object.class))).thenThrow(new RestClientException(""));
        
        pushbulletNotifyAction.execute(new PostDownloadExecution());
    }
    
    @Test
    public void ifRestTemplateFailsBecauseMessageIsUnreadbleThenDoNothing() {
        
        when(mockRestTemplate.exchange(eq("https://api.pushbullet.com/v2/pushes"), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(Object.class))).thenThrow(new HttpMessageConversionException(""));
        
        pushbulletNotifyAction.execute(new PostDownloadExecution());
    }
}
