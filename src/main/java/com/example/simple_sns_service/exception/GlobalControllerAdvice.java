package com.example.simple_sns_service.exception;

import com.example.simple_sns_service.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(SnsApplicationException.class)
    public ResponseEntity<?> applicationHandler(SnsApplicationException error) {
      log.error("Error occurs {}", error.toString());
      return ResponseEntity.status(error.getErrorCode().getStatus())
              .body(Response.error(error.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException error) {
        log.error("Error occurs {}", error.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
