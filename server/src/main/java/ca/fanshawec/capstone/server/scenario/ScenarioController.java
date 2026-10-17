package ca.fanshawec.capstone.server.scenario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios")

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.
public class ScenarioController {
    @Autowired
    private ScenarioRepository scenarioRepository;

    @GetMapping
    public ResponseEntity<Iterable<Scenario>> findAll() {//LF: Likely will be removed in the future.
        Iterable<Scenario> scenarios = scenarioRepository.findAll();
        return new ResponseEntity<Iterable<Scenario>>(scenarios, HttpStatus.OK);
    }
    @GetMapping("/api/scenarios/{id}")
    public ResponseEntity<Scenario> find(@PathVariable long id) {//LF: Will likely require the client or advisor managing the client's encrypted password to be sent over as well to prevent bad actors from accessing scenario data.
        Scenario scenario = scenarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid scenario ID: " + id));
        return new ResponseEntity<Scenario>(scenario, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Scenario> updateOne(@RequestBody Scenario scenario) {//LF: Likely needs input sanitization and validation
        Scenario updatedScenario = scenarioRepository.save(scenario);
        return new ResponseEntity<Scenario>(updatedScenario, HttpStatus.OK);
    }
    @DeleteMapping("/api/scenarios/{id}")
    public ResponseEntity<Integer> deleteOne(@PathVariable long id) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors and a way to delete assets associated with the scenario.
        int deletedCount = scenarioRepository.deleteOne(id);
        return new ResponseEntity<Integer>(deletedCount, HttpStatus.OK);
    }
    @PostMapping("/api/scenarios")
    public ResponseEntity<Scenario> createOne(@RequestBody Scenario scenario) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid scenarios. Password should be encrypted using X25519MLKEM768.
        Scenario newScenario = scenarioRepository.save(scenario);
        return new ResponseEntity<Scenario>(newScenario, HttpStatus.OK);
    }
}