package com.michaeldalby.githubprofileapi.controller;

import com.michaeldalby.githubprofileapi.dto.UserProfileResponseDTO;
import com.michaeldalby.githubprofileapi.service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller for GitHub profile operations. */
@RestController
public class GitHubController {

  private final GitHubService gitHubService;

  public GitHubController(GitHubService gitHubService) {
    this.gitHubService = gitHubService;
  }

  /**
   * Gets a GitHub user's profile by username.
   *
   * @param username the GitHub username
   * @return UserProfileResponseDTO containing the user's profile data
   */
  @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserProfileResponseDTO getUserProfile(@PathVariable String username) {
    if (!StringUtils.hasText(username) || username.contains(" ")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username provided");
    }

    return gitHubService.getUserProfile(username);
  }
}
