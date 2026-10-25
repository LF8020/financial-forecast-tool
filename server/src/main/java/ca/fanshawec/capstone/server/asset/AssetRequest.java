package ca.fanshawec.capstone.server.asset;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//LF: AssetRequest class, this class helps automate some error handling and authentication on inputs.
//    I may come back to this in the future to make my own authentication and remodel this to be compatible with encryption.
@JsonIgnoreProperties(ignoreUnknown = true)
@Value 
@AllArgsConstructor
public class AssetRequest {
    @NotNull(message = "Id must not be blank", groups = {IdCheck.class})
    private Long id;
    @NotNull(message = "ClientId must not be blank", groups = {AuthCheck.class})
    private Long clientId;
    @Size(min = 5, max = 99, message = "Password must be longer than 4 characters and shorter than 100", groups = {AuthCheck.class})
    @NotBlank(message = "Password must not be blank", groups = {AuthCheck.class})
    private String clientPassword;
    @NotNull(message = "ScenarioId must not be blank", groups = {InfoCheck.class, UpdateCheck.class})
    private Long scenarioId;
    @Size(max = 49, message = "Name must be shorter than 50 characters", groups = {UpdateCheck.class})
    @NotBlank(message = "Name must not be blank", groups = {UpdateCheck.class})
    private String name;
    @Size(max = 249, message = "Description must be shorter than 250 characters", groups = {UpdateCheck.class})
    @NotBlank(message = "Description must not be blank", groups = {UpdateCheck.class})
    private String description;
    @NotBlank(message = "Worth must not be blank", groups = {UpdateCheck.class})
    private String worth;
    @NotBlank(message = "APR must not be blank", groups = {UpdateCheck.class})
    private String apr;
    @NotBlank(message = "Mortgage must not be blank", groups = {UpdateCheck.class})
    private String mortgage;
    @NotBlank(message = "Interest must not be blank", groups = {UpdateCheck.class})
    private String interest;
    
    public interface AuthCheck {}
    public interface UpdateCheck {}
    public interface IdCheck {}
    public interface InfoCheck {}
}
