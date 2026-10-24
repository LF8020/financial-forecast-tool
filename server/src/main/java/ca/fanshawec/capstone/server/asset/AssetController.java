package ca.fanshawec.capstone.server.asset;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import ca.fanshawec.capstone.server.client.Client;
import ca.fanshawec.capstone.server.client.ClientRepository;
import ca.fanshawec.capstone.server.exception.ItemNotFoundException;
import ca.fanshawec.capstone.server.scenario.Scenario;
import ca.fanshawec.capstone.server.scenario.ScenarioRepository;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor 

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only client passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.

//LF: Controller for Asset. All HTTP paths for asset will be handled here.
public class AssetController {
    private final AssetRepository assetRepository;
    private final ScenarioRepository scenarioRepository;
    private final ClientRepository clientRepository;

    
    //LF: Update an individual asset. Explicitly requires:
    /*
        AssetRequest{
            Long: Id  (assetId)
            Long: clientId
            String: clientPassword
            Long: scenarioId
            String: name
            String: description
            String: worth
            String: apr
            String: mortgage
            String: interest
        }
    */
    @PutMapping
    public ResponseEntity<AssetReturn> updateOne(@Validated({AssetRequest.IdCheck.class, AssetRequest.AuthCheck.class, AssetRequest.UpdateCheck.class}) @PathVariable AssetRequest assetReq) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors and a way to delete assets associated with the scenario.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(assetReq.getClientId(), assetReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + assetReq.getClientId()));
        Scenario scenarioCheck = scenarioRepository.findByClient(assetReq.getScenarioId(), clientCheck.getId())
            .orElseThrow(() -> new ItemNotFoundException("Scenario doesn't exist or client doesn't have access. ScenarioId: " + assetReq.getScenarioId()));
        Asset assetCheck = assetRepository.findById(assetReq.getId()).orElseThrow(() -> new ItemNotFoundException("Asset doesn't exist in database"));
        assetCheck = assetRepository.save(new Asset(assetReq.getId(), scenarioCheck, assetReq.getName(), assetReq.getDescription(), assetReq.getWorth(), assetReq.getApr(), assetReq.getMortgage(), assetReq.getInterest()));
        AssetReturn returnAsset = new AssetReturn(assetCheck.getId(), scenarioCheck.getId(), assetCheck.getName(), assetCheck.getDescription(), assetCheck.getWorth(), assetCheck.getApr(), assetCheck.getMortgage(), assetCheck.getInterest());
        //LF: TODO: Encrypt Response
        return new ResponseEntity<AssetReturn>(returnAsset, HttpStatus.OK);
    }

    //LF: Delete an individual asset. Explicitly requires:
    /*
        AssetRequest{
            Long: Id  (assetId)
            Long: clientId
            String: clientPassword
        }
    */
    @DeleteMapping("/api/assets")
    public ResponseEntity<Boolean> deleteOne(@Validated({AssetRequest.IdCheck.class, AssetRequest.AuthCheck.class}) @PathVariable AssetRequest assetReq) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors and a way to delete assets associated with the scenario.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(assetReq.getClientId(), assetReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + assetReq.getClientId()));
        scenarioRepository.findByClient(assetReq.getScenarioId(), clientCheck.getId())
            .orElseThrow(() -> new ItemNotFoundException("Scenario doesn't exist or client doesn't have access. ScenarioId: " + assetReq.getScenarioId()));
        boolean deleted = assetRepository.delete(assetReq.getId());
        //LF: TODO: Encrypt response? Maybe? Probably not.
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    //LF: Create an asset. Explicitly requires:
    /*
        AssetRequest{
            Long: clientId
            String: clientPassword
            Long: scenarioId
            String: name
            String: description
            String: worth
            String: apr
            String: mortgage
            String: interest
        }
    */
    @PostMapping("/api/assets")
    public ResponseEntity<AssetReturn> createOne(@Validated({AssetRequest.AuthCheck.class, AssetRequest.UpdateCheck.class}) @RequestBody AssetRequest assetReq) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid scenarios. Password should be encrypted using X25519MLKEM768.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        Client clientCheck = clientRepository.findByAuth(assetReq.getClientId(), assetReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + assetReq.getClientId()));
        Scenario scenarioCheck = scenarioRepository.findByClient(assetReq.getScenarioId(), clientCheck.getId())
            .orElseThrow(() -> new ItemNotFoundException("Scenario doesn't exist or client doesn't have access. ScenarioId: " + assetReq.getScenarioId()));
        Asset newAsset = assetRepository.save(new Asset(scenarioCheck, assetReq.getName(), assetReq.getDescription(), assetReq.getWorth(), assetReq.getApr(), assetReq.getMortgage(), assetReq.getInterest()));
        
        //LF: TODO: Encrypt response
        AssetReturn returnAsset = new AssetReturn(
            newAsset.getId(), 
            scenarioCheck.getId(), 
            newAsset.getName(), 
            newAsset.getDescription(), 
            newAsset.getWorth(), 
            newAsset.getApr(), 
            newAsset.getMortgage(), 
            newAsset.getInterest()
        );
        return new ResponseEntity<AssetReturn>(returnAsset, HttpStatus.OK);
    }

    //LF: Get all assets related to a scenario. Explicitly requires:
    /*
        AssetRequest{
            Long: clientId
            String: clientPassword
            Long: scenarioId
        }
    */
    @PostMapping("/api/assets/get")
    public ResponseEntity<List<AssetReturn>> getScenarios(@Validated({AssetRequest.AuthCheck.class, AssetRequest.InfoCheck.class}) @RequestBody AssetRequest assetReq) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        //LF: TODO: Decrypt request
        //LF: TODO: Add advisor handling
        clientRepository.findByAuth(assetReq.getClientId(), assetReq.getClientPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + assetReq.getClientId()));
        List<AssetReturn> assetList = assetRepository.findAssetsById(assetReq.getScenarioId()).orElse(Collections.emptyList());
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<List<AssetReturn>>(assetList, HttpStatus.OK);        
    }
}