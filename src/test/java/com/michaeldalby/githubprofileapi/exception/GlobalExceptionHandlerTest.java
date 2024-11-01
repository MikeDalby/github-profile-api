package com.michaeldalby.githubprofileapi.exception;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.michaeldalby.githubprofileapi.controller.GitHubController;
import com.michaeldalby.githubprofileapi.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(GitHubController.class)
public class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GitHubService gitHubService;

  @Test
  void handleUserNotFoundExceptionReturnsNotFound() throws Exception {
    String username = "nonexistent";

    Mockito.when(gitHubService.getUserProfile(username))
        .thenThrow(new UserNotFoundException("User not found: " + username));

    mockMvc
        .perform(get("/users/{username}", username))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.error", is("Not Found")))
        .andExpect(jsonPath("$.message", is("User not found: " + username)));
  }

  @Test
  void handleResponseStatusExceptionReturnsBadRequest() throws Exception {
    String invalidUsername = "invalid username";

    Mockito.when(gitHubService.getUserProfile(invalidUsername))
        .thenThrow(
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username provided"));

    mockMvc
        .perform(get("/users/{username}", invalidUsername))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.error", is("Bad Request")))
        .andExpect(jsonPath("$.message", is("Invalid username provided")));
  }
}
