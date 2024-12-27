package de.yehorsh.customerservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvisor {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorData> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorData errorData = new ErrorData(HttpStatus.BAD_REQUEST, ex.getMessage());
        log.debug(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errorData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ErrorData> handleDuplicateFieldException(DuplicateFieldException ex) {
        ErrorData errorData = new ErrorData(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(errorData, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(FieldError::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining("; "));
        log.debug("Validation errors: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }
}
