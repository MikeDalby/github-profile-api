package com.michaeldalby.githubprofileapi.config;

import com.michaeldalby.githubprofileapi.client.GitHubApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/** Configuration class for application beans. */
@Configuration
public class AppConfig {

  private final String githubApiUrl;

  public AppConfig(@Value("${github.api.url}") String githubApiUrl) {
    this.githubApiUrl = githubApiUrl;
  }

  /**
   * Creates a WebClient bean configured with the GitHub API URL.
   *
   * @return the configured WebClient
   */
  @Bean
  public WebClient webClient() {
    return WebClient.builder().baseUrl(githubApiUrl).build();
  }

  /**
   * Creates a GitHubApiClient bean.
   *
   * @param webClient the WebClient used for making HTTP requests
   * @return a GitHubApiClient instance
   */
  @Bean
  public GitHubApiClient gitHubApiClient(WebClient webClient) {
    HttpServiceProxyFactory factory =
        HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
    return factory.createClient(GitHubApiClient.class);
  }
}
