package ca.fanshawec.capstone.server.asset;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//LF: AssetRequest class, this class helps automate some error handling and authentication on inputs.
//    I may come back to this in the future to make my own authentication and remodel this to be compatible with encryption.
@JsonIgnoreProperties(ignoreUnknown = true)
@Value 
@AllArgsConstructor
public class AssetRequest {
    @JsonProperty("id")
    @NotNull(message = "Id must not be null", groups = {IdCheck.class})
    private Long id;
    @JsonProperty("clientId")
    @NotNull(message = "ClientId must not be null", groups = {AuthCheck.class})
    private Long clientId;
    @JsonProperty("clientPassword")
    @Size(min = 5, max = 99, message = "Password must be longer than 4 characters and shorter than 100", groups = {AuthCheck.class})
    @NotBlank(message = "Password must not be blank", groups = {AuthCheck.class})
    private String clientPassword;
    @JsonProperty("scenarioId")
    @NotNull(message = "ScenarioId must not be blank", groups = {InfoCheck.class, UpdateCheck.class})
    private Long scenarioId;
    @JsonProperty("name")
    @Size(max = 49, message = "Name must be shorter than 50 characters", groups = {UpdateCheck.class})
    @NotBlank(message = "Name must not be blank", groups = {UpdateCheck.class})
    private String name;
    @JsonProperty("description")
    @Size(max = 249, message = "Description must be shorter than 250 characters", groups = {UpdateCheck.class})
    @NotBlank(message = "Description must not be blank", groups = {UpdateCheck.class})
    private String description;
    @JsonProperty("worth")
    @NotBlank(message = "Worth must not be blank", groups = {UpdateCheck.class})
    private String worth;
    @JsonProperty("apr")
    @NotBlank(message = "APR must not be blank", groups = {UpdateCheck.class})
    private String apr;
    @JsonProperty("mortgage")
    @NotBlank(message = "Mortgage must not be blank", groups = {UpdateCheck.class})
    private String mortgage;
    @JsonProperty("interest")
    @NotBlank(message = "Interest must not be blank", groups = {UpdateCheck.class})
    private String interest;
    
    public interface AuthCheck {}
    public interface UpdateCheck {}
    public interface IdCheck {}
    public interface InfoCheck {}
}
