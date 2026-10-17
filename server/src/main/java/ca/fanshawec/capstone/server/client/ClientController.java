package ca.fanshawec.capstone.server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/clients")

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<Iterable<Client>> findAll() {//LF: Likely will be removed in the future.
        Iterable<Client> clients = clientRepository.findAll();
        return new ResponseEntity<Iterable<Client>>(clients, HttpStatus.OK);
    }
    @GetMapping("/api/clients/{id}")
    public ResponseEntity<Client> find(@PathVariable long id) {//LF: Will likely require the client or advisor managing the client's encrypted password to be sent over as well to prevent bad actors from accessing client data.
        Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client ID: " + id));
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }
    @PostMapping("/api/clients/login")//Login path, password needs to be encrypted before transit and decrypted after.
    public ResponseEntity<Client> findByLogin(@RequestBody Client client) {//LF: Will likely require the client or advisor managing the client's encrypted password to be sent over as well to prevent bad actors from accessing client data.
        Client actualClient = clientRepository.findByLogin(client.getEmail(), client.getPassword()).orElseThrow(() -> new IllegalArgumentException("Invalid client credentials: " + client.getEmail()));
        return new ResponseEntity<Client>(actualClient, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Client> updateOne(@RequestBody Client client) {//LF: Likely needs input sanitization and validation
        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<Client>(updatedClient, HttpStatus.OK);
    }
    @DeleteMapping("/api/clients/{id}")
    public ResponseEntity<Integer> deleteOne(@PathVariable long id) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors and a way to delete scenarios and assets associated with the client.
        int deletedCount = clientRepository.deleteOne(id);
        return new ResponseEntity<Integer>(deletedCount, HttpStatus.OK);
    }
    @PostMapping("/api/clients")
    public ResponseEntity<Client> createOne(@RequestBody Client client) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted using X25519MLKEM768.
        Client newClient = clientRepository.save(client);
        return new ResponseEntity<Client>(newClient, HttpStatus.OK);
    }
}