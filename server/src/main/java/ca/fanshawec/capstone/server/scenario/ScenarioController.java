package ca.fanshawec.capstone.server.scenario;

import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ca.fanshawec.capstone.server.client.Client;
import ca.fanshawec.capstone.server.client.ClientRepository;
import ca.fanshawec.capstone.server.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios")
@RequiredArgsConstructor

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.
//LF: Controller for Scenario. All HTTP paths for scenario will be handled here.
public class ScenarioController {
    private final ScenarioRepository scenarioRepository;
    private final ClientRepository clientRepository;


    //LF: Update an individual scenario. Explicitly requires:
    /*
        AssetRequest{
            Long: Id  (scenarioId)
            Long: clientId
            String: clientPassword
            String: name
            String: description
        }
    */
    @PutMapping
    public ResponseEntity<ScenarioReturn> updateOne(@Validated({ScenarioRequest.AuthCheck.class, ScenarioRequest.UpdateCheck.class, ScenarioRequest.IdCheck.class}) @RequestBody ScenarioRequest scenarioReq) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors and a way to delete assets associated with the scenario.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(scenarioReq.getClientId(), scenarioReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + scenarioReq.getClientId()));
        Scenario scenarioCheck = scenarioRepository.findByClient(scenarioReq.getId(), clientCheck.getId())
            .orElseThrow(() -> new ItemNotFoundException("Scenario doesn't exist or client doesn't have access. ScenarioId: " + scenarioReq.getId()));
        scenarioCheck = scenarioRepository.save(new Scenario(scenarioCheck.getId(), scenarioCheck.getClient(), scenarioReq.getName(), scenarioReq.getDescription()));
        ScenarioReturn returnScenario = new ScenarioReturn(scenarioCheck.getId(), scenarioCheck.getName(), scenarioCheck.getDescription());
        //LF: TODO: Encrypt Response
        return new ResponseEntity<ScenarioReturn>(returnScenario, HttpStatus.OK);
    }

    //LF: Deletes an individual scenario. Explicitly requires:
    /*
        AssetRequest{
            Long: Id  (scenarioId)
            Long: clientId
            String: clientPassword
        }
    */
    @DeleteMapping("/api/scenarios")
    public ResponseEntity<Boolean> deleteOne(@Validated({ScenarioRequest.IdCheck.class, ScenarioRequest.AuthCheck.class}) @RequestBody ScenarioRequest scenarioReq) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors and a way to delete assets associated with the scenario.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(scenarioReq.getClientId(), scenarioReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + scenarioReq.getClientId()));
        Scenario scenarioCheck = scenarioRepository.findByClient(scenarioReq.getId(), clientCheck.getId())
            .orElseThrow(() -> new ItemNotFoundException("Scenario doesn't exist or client doesn't have access. ScenarioId: " + scenarioReq.getId()));
        boolean deleted = scenarioRepository.delete(scenarioCheck.getId());
        //LF: TODO: Encrypt response? Maybe? Probably not.
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    //LF: Creates an individual scenario. Explicitly requires:
    /*
        AssetRequest{
            Long: clientId
            String: clientPassword
            String: name
            String: description
        }
    */
    @PostMapping("/api/scenarios")
    public ResponseEntity<ScenarioReturn> createOne(@Validated({ScenarioRequest.AuthCheck.class, ScenarioRequest.UpdateCheck.class}) @RequestBody ScenarioRequest scenarioReq) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid scenarios. Password should be encrypted using X25519MLKEM768.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(scenarioReq.getClientId(), scenarioReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + scenarioReq.getClientId()));
        Scenario newScenario = scenarioRepository.save(new Scenario(clientCheck, scenarioReq.getName(), scenarioReq.getDescription()));
        ScenarioReturn returnScenario = new ScenarioReturn(newScenario.getId(), newScenario.getName(), newScenario.getDescription());
        //LF: TODO: Encrypt response
        return new ResponseEntity<ScenarioReturn>(returnScenario, HttpStatus.OK);
    }

    //LF: Gets all scenarios associated with a client. Explicitly requires:
    /*
        AssetRequest{
            Long: clientId
            String: clientPassword
        }
    */
    @PostMapping("/api/scenarios/get")
    public ResponseEntity<List<ScenarioReturn>> getScenarios(@Validated({ScenarioRequest.AuthCheck.class}) @RequestBody ScenarioRequest scenarioReq) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(scenarioReq.getClientId(), scenarioReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + scenarioReq.getClientId()));
        List<ScenarioReturn> scenarioList = scenarioRepository.findScenariosById(clientCheck.getId()).orElse(Collections.emptyList());
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<List<ScenarioReturn>>(scenarioList, HttpStatus.OK);        
    }
}