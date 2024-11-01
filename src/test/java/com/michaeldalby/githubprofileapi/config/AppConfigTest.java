package com.michaeldalby.githubprofileapi.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.michaeldalby.githubprofileapi.client.GitHubApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class AppConfigTest {

  @Autowired private WebClient webClient;

  @Autowired private GitHubApiClient gitHubApiClient;

  @Test
  void webClientBeanIsConfigured() {
    // There isn't a great way to verify this object
    assertNotNull(webClient);
  }

  @Test
  void gitHubApiClientBeanIsConfigured() {
    // There isn't a great way to verify this object
    assertNotNull(gitHubApiClient);
  }
}
