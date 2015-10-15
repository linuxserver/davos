package io.linuxserver.davos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import io.linuxserver.davos.DavosApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DavosApplication.class)
@WebAppConfiguration
public class DavosApplicationTest {

	@Test
	public void contextLoads() {
	}
}
