package ca.fanshawec.capstone.server.client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


//LF: ClientRequest class, this class helps automate some error handling and authentication on inputs.
//    I may come back to this in the future to make my own authentication and remodel this to be compatible with encryption.
@JsonIgnoreProperties(ignoreUnknown = true)
@Value 
@AllArgsConstructor
public class ClientRequest {
    @NotBlank(message = "Id must not be blank", groups = {FullCheck.class, AuthCheck.class})
    private Long id;
    @Size(min = 5, max = 99, message = "Name must be longer than 4 characters and shorter than 100", groups = {FullCheck.class, RegisterCheck.class})
    @NotBlank(message = "Name must not be blank", groups = {FullCheck.class, RegisterCheck.class})
    private String name;
    @Size(min = 5, max = 99, message = "Email must be longer than 4 characters and shorter than 100", groups = {FullCheck.class, LoginCheck.class, RegisterCheck.class})
    @NotBlank(message = "Email must not be blank", groups = {FullCheck.class, LoginCheck.class, RegisterCheck.class})
    @Email(message = "Email must be in the \"*@*.*\" format.", groups = {FullCheck.class, LoginCheck.class, RegisterCheck.class})
    private String email;
    @NotBlank(message = "Phone must not be blank", groups = {FullCheck.class, RegisterCheck.class})
    private String phone;
    @Size(min = 5, max = 99, message = "Password must be longer than 4 characters and shorter than 100", groups = {FullCheck.class, LoginCheck.class, AuthCheck.class, RegisterCheck.class})
    @NotBlank(message = "Password must not be blank", groups = {FullCheck.class, LoginCheck.class, AuthCheck.class, RegisterCheck.class})
    private String password;
    @Size(min = 5, max = 99, message = "Password must be longer than 4 characters and shorter than 100", groups = FullCheck.class)
    @NotBlank(message = "Password must not be blank", groups = FullCheck.class)
    private String secondPassword;

    public interface LoginCheck {}
    public interface RegisterCheck {}
    public interface FullCheck {}
    public interface AuthCheck {}
}
