package ca.fanshawec.capstone.server.exception;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Data;

//LF: Class to define an API error to send back when an exception is caught.
@Data 
public class APIError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;


    //LF: Includes a list of field errors for wrong input when mapping HTTP requests to Request objects for their path.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldErrorDetails> fieldErrors;


    //LF: This constructor accepts HTTPStatus, error title, error message, path.
    //    Constructs the timestamp by itself in epoch milliseconds.
    public APIError(int status, String error, String message, String path){
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    //Definition for the field errors class, includes the field, the value and the reason it was rejected.
    @Data
    public static class FieldErrorDetails {
        private String field;
        private String rejectedValue;
        private String reason;

        public FieldErrorDetails(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue != null ? rejectedValue.toString() : "null";
            this.reason = reason;
        }
    }
}
