package com.michaeldalby.githubprofileapi.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.michaeldalby.githubprofileapi.client.GitHubApiClient;
import com.michaeldalby.githubprofileapi.dto.GitHubRepositoryDTO;
import com.michaeldalby.githubprofileapi.dto.GitHubUserDTO;
import com.michaeldalby.githubprofileapi.dto.UserProfileResponseDTO;
import com.michaeldalby.githubprofileapi.exception.UserNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

  @Mock private GitHubApiClient gitHubApiClient;

  @InjectMocks private GitHubService service;

  @Test
  void getUserProfileReturnsProfile() throws ExecutionException, InterruptedException {
    String username = "octocat";
    GitHubUserDTO mockUser =
        new GitHubUserDTO(
            "octocat",
            "The Octocat",
            "avatar_url",
            "San Francisco",
            "octo@example.com",
            "html_url",
            "created_at");

    List<GitHubRepositoryDTO> mockRepos = List.of(new GitHubRepositoryDTO("repo_name", "repo_url"));

    when(gitHubApiClient.getUser(username)).thenReturn(mockUser);
    when(gitHubApiClient.getUserRepositories(username)).thenReturn(mockRepos);

    UserProfileResponseDTO profile = service.getUserProfile(username);

    assertNotNull(profile);
  }

  @Test
  void getUserProfileHandlesException() {
    String username = "octocat";

    when(gitHubApiClient.getUser(username)).thenThrow(new RuntimeException("GitHub API failed"));

    assertThrows(
        RuntimeException.class,
        () -> service.getUserProfile(username),
        "Failed to fetch data from GitHub API");
  }

  @Test
  void getUserProfileThrowsUserNotFoundException() {
    String username = "nonexistentuser";

    // Simulate `NotFound` exception for `getUser`
    WebClientResponseException notFoundException =
        WebClientResponseException.create(404, "Not Found", null, null, StandardCharsets.UTF_8);

    when(gitHubApiClient.getUser(username)).thenThrow(notFoundException);
    when(gitHubApiClient.getUserRepositories(username)).thenThrow(notFoundException);

    assertThrows(
        UserNotFoundException.class,
        () -> service.getUserProfile(username),
        "User not found: " + username);
  }
}
