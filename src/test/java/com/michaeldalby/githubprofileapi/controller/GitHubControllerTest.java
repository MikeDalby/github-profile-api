package com.michaeldalby.githubprofileapi.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaeldalby.githubprofileapi.dto.UserProfileResponseDTO;
import com.michaeldalby.githubprofileapi.service.GitHubService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GitHubController.class)
public class GitHubControllerTest {

  @MockBean private GitHubService gitHubService;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void getUserProfileReturnsSuccess() throws Exception {
    String username = "octocat";

    // Mock response
    UserProfileResponseDTO mockResponse =
        UserProfileResponseDTO.builder()
            .user_name("octocat")
            .display_name("The Octocat")
            .avatar("avatar_url")
            .geo_location("San Francisco")
            .email("octo@example.com")
            .url("html_url")
            .created_at("created_at")
            .repos(List.of(new UserProfileResponseDTO.Repo("repo_name", "repo_url")))
            .build();

    when(gitHubService.getUserProfile(username)).thenReturn(mockResponse);

    mockMvc
        .perform(get("/users/{username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user_name", is("octocat")))
        .andExpect(jsonPath("$.display_name", is("The Octocat")))
        .andExpect(jsonPath("$.avatar", is("avatar_url")))
        .andExpect(jsonPath("$.geo_location", is("San Francisco")))
        .andExpect(jsonPath("$.email", is("octo@example.com")))
        .andExpect(jsonPath("$.url", is("html_url")))
        .andExpect(jsonPath("$.created_at", is("created_at")))
        .andExpect(jsonPath("$.repos[0].name", is("repo_name")))
        .andExpect(jsonPath("$.repos[0].url", is("repo_url")));
  }

  @Test
  void getUserProfileReturnsBadRequestForInvalidUsername() throws Exception {
    String invalidUsername = "invalid username";

    mockMvc
        .perform(get("/users/{username}", invalidUsername))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.error", is("Bad Request")))
        .andExpect(jsonPath("$.message", is("Invalid username provided")));
  }

  @Test
  void getUserProfileReturnsBadRequestForEmptyUsername() throws Exception {
    // Use a space to simulate the empty/missing username scenario
    mockMvc
        .perform(get("/users/ "))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.error", is("Bad Request")))
        .andExpect(jsonPath("$.message", is("Invalid username provided")));
  }

  @Test
  void getUserProfileReturnsBadRequestForUsernameWithSpaces() throws Exception {
    String usernameWithSpaces = "invalid username";

    mockMvc
        .perform(get("/users/{username}", usernameWithSpaces))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.error", is("Bad Request")))
        .andExpect(jsonPath("$.message", is("Invalid username provided")));
  }

  @Test
  void getUserProfileReturnsInternalServerErrorForGeneralException() throws Exception {
    String username = "octocat";

    when(gitHubService.getUserProfile(username))
        .thenThrow(new RuntimeException("Unexpected error"));

    mockMvc
        .perform(get("/users/{username}", username))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.status", is(500)))
        .andExpect(jsonPath("$.error", is("Internal Server Error")))
        .andExpect(jsonPath("$.message", is("An unexpected error occurred")));
  }
}
