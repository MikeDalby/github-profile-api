package com.michaeldalby.githubprofileapi.exception;

/** Custom exception for user not found scenarios. */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
