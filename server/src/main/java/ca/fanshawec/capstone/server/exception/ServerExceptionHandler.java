package ca.fanshawec.capstone.server.exception;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

//LF: Class to handle all exceptions from both bad client input and malicious requests made without the client.
//TODO: Maybe some sort of defense against DOS attacks and session tokens for users in the future.
@RestControllerAdvice 
public class ServerExceptionHandler{

    //LF: This handler should handle most errors related to fields being wrong.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
            APIError apiError = new APIError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "The request body contains invalid parameters.",
            request.getRequestURI()
        );
        List<APIError.FieldErrorDetails> fields = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(
                error -> new APIError.FieldErrorDetails(
                    error.getField(),
                    error.getRejectedValue(),
                    error.getDefaultMessage()
                )
            )
            .collect(Collectors.toList());

        apiError.setFieldErrors(fields);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    //LF: Handles times when JSON data doesn't match expected input.
    @ExceptionHandler (HttpMessageNotReadableException.class)
    public ResponseEntity<APIError> handleMalformedJson(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        APIError apiError = new APIError(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON Request",
                "The server could not read or parse the request body syntax.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    //LF: Handles ItemNotFound exception thrown when something isn't found in the database.
    @ExceptionHandler (ItemNotFoundException.class)
    public ResponseEntity<APIError> handleNotFound(
            ItemNotFoundException ex, HttpServletRequest request) {
        
        APIError apiError = new APIError(
                HttpStatus.NOT_FOUND.value(),
                "Item not found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    //LF: Handles Unauthorized exception thrown when scenarios are accessed without the proper credentials.
    //TODO: Deprecated at the moment but will be added back with Advisor handling. 
    @ExceptionHandler (UnauthorizedException.class)
    public ResponseEntity<APIError> handleNotFound(
            UnauthorizedException ex, HttpServletRequest request) {
        
        APIError apiError = new APIError(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized Access",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    //LF: DuplicateEntry exception thrown when a duplicate entry exists already exists in the database when trying to update/add another item
    @ExceptionHandler (DuplicateEntryException.class)
    public ResponseEntity<APIError> handleNotFound(
            DuplicateEntryException ex, HttpServletRequest request) {
        
        APIError apiError = new APIError(
                HttpStatus.CONFLICT.value(),
                "Duplicate entry in database",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}