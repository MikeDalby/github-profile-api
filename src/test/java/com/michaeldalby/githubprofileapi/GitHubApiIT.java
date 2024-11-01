package com.michaeldalby.githubprofileapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.michaeldalby.githubprofileapi.dto.UserProfileResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubApiIT {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void getUserProfileIntegrationTest() {

    // TODO: Create a GitHub user who's data we control
    //   specifically for these tests
    String username = "octocat";

    // Build the URL for the request
    String url = "http://localhost:" + port + "/users/" + username;

    // Make the GET request to the running server
    ResponseEntity<UserProfileResponseDTO> response =
        restTemplate.getForEntity(url, UserProfileResponseDTO.class);

    // Assert that the response status is OK
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    UserProfileResponseDTO responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    assertThat(responseBody.getUser_name()).isEqualTo(username);
    assertThat(responseBody.getAvatar()).isNotEmpty();
    assertThat(responseBody.getGeo_location()).isNotNull();
    assertThat(responseBody.getRepos()).isNotEmpty();
  }
}
