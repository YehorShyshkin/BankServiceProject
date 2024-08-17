package com.bankapp.app.exception_handler;

import com.bankapp.app.exception.AgreementNotFoundException;
import com.bankapp.app.exception.ClientNotFoundException;
import com.bankapp.app.exception.ManagerNotFoundException;
import com.bankapp.app.exception.ProductNotFoundException;
import com.bankapp.app.request.ErrorData;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Arrays;

@ControllerAdvice
@Profile("!debug")
public class ExceptionControllerAdvisor extends Exception {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorData> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorData errorData = new ErrorData(HttpStatus.BAD_REQUEST, OffsetDateTime.now(),
                exception.getMessage(), Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(errorData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ManagerNotFoundException.class,
            ClientNotFoundException.class,
            ProductNotFoundException.class,
            AgreementNotFoundException.class})
    public ResponseEntity<ErrorData> handleEntityNotFoundException(RuntimeException exception) {
        ErrorData errorData = new ErrorData(HttpStatus.NOT_FOUND, OffsetDateTime.now(),
                exception.getMessage(), Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(errorData, HttpStatus.NOT_FOUND);
    }
}
