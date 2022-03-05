package smalldomains.domainmanager.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import smalldomains.domainmanager.exception.NoSmallDomainExists;
import smalldomains.domainmanager.exception.SmallDomainAlreadyExists;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This application has been designed to throw the correct error HTTP codes/messages when various different exceptions occur
 * This creates a new ExceptionHandler for each type of exception which may be thrown. Each ExceptionHandler composes the most appropriate
 * HTTP response code and messages.
 *
 * The ControllerAdvice annotation means that these handlers apply globally - to all controllers
 */
@Log4j2
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(SmallDomainAlreadyExists.class)
    public ResponseEntity<Map<String, String>> handleSmallDomainAlreadyExists(final SmallDomainAlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(generateErrorBody(String.format("small domain already exists: %s", ex.getAlreadyExistingDomain())));
    }

    @ExceptionHandler(NoSmallDomainExists.class)
    public ResponseEntity<Map<String, String>> handleNoSmallDomainExists(final NoSmallDomainExists ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(generateErrorBody(ex.getNameOfNonExistentDomain() + " not found"));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(final WebExchangeBindException ex) {
        final Map<String, Object> mutableResponse = new HashMap<>(
            generateErrorBody("Bad Request - Check \"validationErrors\" for more details")
        );

        mutableResponse.put("validationErrors", generateValidationErrors(ex));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mutableResponse);
    }

    private Map<String, String> generateErrorBody(final String errorMessage) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", errorMessage
        );
    }

    private Map<String, String> generateValidationErrors(final WebExchangeBindException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fieldError -> {
                    final String message = fieldError.getDefaultMessage();
                    if(message == null) {
                        log.error("No validation message has been set up for field. Can you set this up to improve UX? {}", fieldError);
                        return "invalid value";
                    } else {
                        return message;
                    }
                }));
    }

}
