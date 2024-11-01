package com.michaeldalby.githubprofileapi.service;

import com.michaeldalby.githubprofileapi.client.GitHubApiClient;
import com.michaeldalby.githubprofileapi.dto.GitHubRepositoryDTO;
import com.michaeldalby.githubprofileapi.dto.GitHubUserDTO;
import com.michaeldalby.githubprofileapi.dto.UserProfileResponseDTO;
import com.michaeldalby.githubprofileapi.exception.UserNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Service for retrieving GitHub user profile data. */
@Service
public class GitHubService {

  private final GitHubApiClient gitHubApiClient;

  public GitHubService(GitHubApiClient gitHubApiClient) {
    this.gitHubApiClient = gitHubApiClient;
  }

  /**
   * Retrieves a user's profile from GitHub, including repository information.
   *
   * @param username the GitHub username
   * @return UserProfileResponseDTO containing profile data and repositories
   */
  @Cacheable("userProfiles")
  public UserProfileResponseDTO getUserProfile(String username) {

    // Call getUser and getUserRepositories in parallel, instead of serially.
    // Similar to creating two Promises
    // TODO: Consider the new StructuredConcurrency feature
    CompletableFuture<GitHubUserDTO> userFuture =
        CompletableFuture.supplyAsync(() -> gitHubApiClient.getUser(username));
    CompletableFuture<List<GitHubRepositoryDTO>> reposFuture =
        CompletableFuture.supplyAsync(() -> gitHubApiClient.getUserRepositories(username));

    try {
      // Wait for both futures to complete, similar to Promise.all.
      // The join() method will wrap the errors we really care about in
      // a CompletionException, so we'll unpack it immediately.
      try {
        CompletableFuture.allOf(userFuture, reposFuture).join();
      } catch (CompletionException e) {
        throw e.getCause();
      }

      // Get results from futures
      GitHubUserDTO ghUser = userFuture.get();
      List<GitHubRepositoryDTO> ghRepoList = reposFuture.get();

      // Map the GH Repos to our Response Repo List
      List<UserProfileResponseDTO.Repo> responseRepoList =
          ghRepoList.stream()
              .map(repo -> new UserProfileResponseDTO.Repo(repo.name(), repo.html_url()))
              .toList();

      // Map the entire GH Profile to our Response Profile
      return UserProfileResponseDTO.builder()
          .user_name(ghUser.login())
          .display_name(ghUser.name())
          .avatar(ghUser.avatar_url())
          .geo_location(ghUser.location())
          .email(ghUser.email())
          .url(ghUser.html_url())
          .created_at(ghUser.created_at())
          .repos(responseRepoList)
          .build();

    } catch (WebClientResponseException.NotFound e) {
      throw new UserNotFoundException("User not found: " + username);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to fetch data from GitHub API", e);
    }
  }
}
