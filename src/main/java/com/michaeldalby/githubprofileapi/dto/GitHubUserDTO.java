package com.michaeldalby.githubprofileapi.dto;

/**
 * Data Transfer Object representing a GitHub user profile. Contains basic profile information of a
 * GitHub user.
 */
public record GitHubUserDTO(
    String login,
    String name,
    String avatar_url,
    String location,
    String email,
    String html_url,
    String created_at) {}
