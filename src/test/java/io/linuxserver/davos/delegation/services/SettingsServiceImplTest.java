package io.linuxserver.davos.delegation.services;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.linuxserver.davos.Version;

public class SettingsServiceImplTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> entityCaptor;

    @InjectMocks
    private SettingsService settingsService = new SettingsServiceImpl();

    @Before
    public void before() {
        initMocks(this);

        when(mockRestTemplate.exchange(eq("https://raw.githubusercontent.com/linuxserver/davos/LatestRelease/version.txt"),
                eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                        .thenReturn(new ResponseEntity<String>("2.2.2", HttpStatus.OK));
    }

    @Test
    public void checkVersionShouldCallGitHub() {

        settingsService.retrieveRemoteVersion();

        verify(mockRestTemplate).exchange(eq("https://raw.githubusercontent.com/linuxserver/davos/LatestRelease/version.txt"),
                eq(HttpMethod.GET), entityCaptor.capture(), eq(String.class));
    }

    @Test
    public void checkVersionShouldReturnVersionFromGithub() {

        Version version = settingsService.retrieveRemoteVersion();

        assertThat(version.toString()).isEqualTo("2.2.2");
    }

    @Test
    public void ifRestTemplateFailsThenReturnEmptyVersion() {

        when(mockRestTemplate.exchange(eq("https://raw.githubusercontent.com/linuxserver/davos/LatestRelease/version.txt"),
                eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenThrow(new RestClientException(""));

        Version version = settingsService.retrieveRemoteVersion();

        assertThat(version.toString()).isEqualTo("0.0.0");
    }
}
