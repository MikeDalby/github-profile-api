package com.michaeldalby.githubprofileapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object representing the response for a GitHub user profile request. This includes
 * user information as well as a list of repositories associated with the user.
 */
@Builder
@Data
public class UserProfileResponseDTO {
  private final String user_name;
  private final String display_name;
  private final String avatar;
  private final String geo_location;
  private final String email;
  private final String url;
  private final String created_at;
  private final List<Repo> repos;

  /**
   * Inner static class representing a GitHub repository associated with the user.
   */
  @Data
  public static class Repo {
    private final String name;
    private final String url;
  }
}
