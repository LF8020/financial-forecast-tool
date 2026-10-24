package ca.fanshawec.capstone.server.scenario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//LF: ScenarioRequest class, this class helps automate some error handling and authentication on inputs.
//    I may come back to this in the future to make my own authentication and remodel this to be compatible with encryption.
@JsonIgnoreProperties(ignoreUnknown = true)
@Value 
@AllArgsConstructor
public class ScenarioRequest {
    @NotBlank(message = "Id must not be blank", groups = {IdCheck.class})
    private Long id;
    @NotBlank(message = "Name must not be blank", groups = {UpdateCheck.class})
    @Size(min = 1, max = 49, message = "Name must be longer than 4 characters and shorter than 50", groups = {UpdateCheck.class})
    private String name;
    @NotBlank(message = "Description must not be blank", groups = {UpdateCheck.class})
    @Size(min = 1, max = 255, message = "Description must be longer than 4 characters and shorter than 255", groups = {UpdateCheck.class})
    private String description;
    @NotBlank(message = "ClientId must not be blank", groups = {AuthCheck.class})
    private Long clientId;
    @Size(min = 5, max = 99, message = "Password must be longer than 4 characters and shorter than 100", groups = {AuthCheck.class})
    @NotBlank(message = "Password must not be blank", groups = {AuthCheck.class})
    private String clientPassword;

    public interface AuthCheck {}
    public interface UpdateCheck {}
    public interface IdCheck {}
}

