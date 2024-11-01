package com.michaeldalby.githubprofileapi.dto;

/**
 * Data Transfer Object representing a GitHub repository. Contains information about a repository.
 */
public record GitHubRepositoryDTO(String name, String html_url) {}
