package com.michaeldalby.githubprofileapi.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler to handle exceptions across the entire application and return JSON
 * responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles UserNotFoundException by returning a 404 Not Found response.
   *
   * @param ex the exception
   * @return a ResponseEntity with error details and HTTP status
   */
  @ExceptionHandler(UserNotFoundException.class)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
    return buildErrorResponseForJSON(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
  }

  /**
   * Handles ResponseStatusException for custom status responses.
   *
   * @param ex the exception
   * @return a ResponseEntity with error details and HTTP status
   */
  @ExceptionHandler(ResponseStatusException.class)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleResponseStatusException(
      ResponseStatusException ex) {
    return buildErrorResponseForJSON((HttpStatus) ex.getStatusCode(), ex.getReason(), ex);
  }

  /**
   * Handles all other uncaught exceptions, returning a 500 Internal Server Error response.
   *
   * @param ex the exception
   * @return a ResponseEntity with error details and HTTP status
   */
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    return buildErrorResponseForJSON(
        HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex);
  }

  /**
   * Constructs a structured error response for JSON formatting
   *
   * @param status the HTTP status
   * @param message the error message
   * @param ex the exception
   * @return a ResponseEntity containing error details
   */
  private ResponseEntity<Map<String, Object>> buildErrorResponseForJSON(
      HttpStatus status, String message, Exception ex) {
    Map<String, Object> errorBody = new HashMap<>();
    errorBody.put("timestamp", LocalDateTime.now());
    errorBody.put("status", status.value());
    errorBody.put("error", status.getReasonPhrase());
    errorBody.put("message", message);
    return new ResponseEntity<>(errorBody, status);
  }
}
