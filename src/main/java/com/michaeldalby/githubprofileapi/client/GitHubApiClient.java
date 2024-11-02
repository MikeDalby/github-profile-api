package com.michaeldalby.githubprofileapi.client;

import com.michaeldalby.githubprofileapi.dto.GitHubRepositoryDTO;
import com.michaeldalby.githubprofileapi.dto.GitHubUserDTO;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/** Client interface for accessing GitHub's user and repository APIs. */
@HttpExchange
public interface GitHubApiClient {

  /**
   * Retrieves a GitHub user by username.
   *
   * @param username the GitHub username
   * @return a GitHubUserDTO containing user details
   */
  @GetExchange("/users/{username}")
  GitHubUserDTO getUser(@PathVariable String username);

  /**
   * Retrieves a list of repositories for a GitHub user.
   *
   * @param username the GitHub username
   * @return a list of GitHubRepositoryDTO containing repository details
   */
  @GetExchange("/users/{username}/repos")
  List<GitHubRepositoryDTO> getUserRepositories(@PathVariable String username);
}
