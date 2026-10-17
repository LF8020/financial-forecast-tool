package ca.fanshawec.capstone.server.advisor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/advisors")

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only client passwords but also other sensitive data such as the financial information contained in scenarios and assets as well as the information associated with clients that the advisor manages.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.
public class AdvisorController {
    @Autowired
    private AdvisorRepository advisorRepository;

    @GetMapping
    public ResponseEntity<Iterable<Advisor>> findAll() {//LF: Likely will be removed in the future.
        Iterable<Advisor> advisors = advisorRepository.findAll();
        return new ResponseEntity<Iterable<Advisor>>(advisors, HttpStatus.OK);
    }
    @GetMapping("/api/advisors/{id}")
    public ResponseEntity<Advisor> find(@PathVariable long id) {//LF: Will likely require the advisor's encrypted password to be sent over as well to prevent bad actors from accessing advisor data and their clients.
        Advisor advisor = advisorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid advisor ID: " + id));
        return new ResponseEntity<Advisor>(advisor, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Advisor> updateOne(@RequestBody Advisor advisor) {//LF: Likely needs input sanitization and validation
        Advisor updatedAdvisor = advisorRepository.save(advisor);
        return new ResponseEntity<Advisor>(updatedAdvisor, HttpStatus.OK);
    }
    @DeleteMapping("/api/advisors/{id}")
    public ResponseEntity<Integer> deleteOne(@PathVariable long id) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors.
        int deletedCount = advisorRepository.deleteOne(id);
        return new ResponseEntity<Integer>(deletedCount, HttpStatus.OK);
    }
    @PostMapping("/api/advisors")
    public ResponseEntity<Advisor> createOne(@RequestBody Advisor advisor) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid advisors. Password should be encrypted using X25519MLKEM768.
        Advisor newAdvisor = advisorRepository.save(advisor);
        return new ResponseEntity<Advisor>(newAdvisor, HttpStatus.OK);
    }
}