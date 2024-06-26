package az.unibank.uniTech.v1.handler;

import az.unibank.uniTech.v1.exception.InvalidCredentialsException;
import az.unibank.uniTech.v1.exception.InvalidTokenException;
import az.unibank.uniTech.v1.exception.PinAlreadyRegisteredExcetion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.channels.AlreadyBoundException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            PinAlreadyRegisteredExcetion.class,
            InvalidCredentialsException.class ,
            InvalidTokenException.class
    })
    public ResponseEntity<ExceptionResponse> handleClientException(RuntimeException ex) {
        var result = ExceptionResponse.builder().message(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleOrientException(MethodArgumentNotValidException ex) {
        List<FieldValidationError> fieldErrors = ex.getFieldErrors().stream()
                .map(e->FieldValidationError.builder()
                        .errorMessage(e.getDefaultMessage())
                        .field(e.getField())
                        .build())
                .toList();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .validations(fieldErrors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }
}
