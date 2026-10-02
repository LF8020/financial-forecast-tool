package ca.fanshawec.capstone.server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<Iterable<Client>> findAll() {
        Iterable<Client> clients = clientRepository.findAll();
        return new ResponseEntity<Iterable<Client>>(clients, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Client> updateOne(@RequestBody Client client) {
        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<Client>(updatedClient, HttpStatus.OK);
    }
    @DeleteMapping("/api/clients/{id}")
    public ResponseEntity<Integer> deleteOne(@PathVariable long id) {
        int deletedCount = clientRepository.deleteOne(id);
        return new ResponseEntity<Integer>(deletedCount, HttpStatus.OK);
    }
    @PostMapping("/api/clients")
    public ResponseEntity<Client> createOne(@RequestBody Client client) {
        Client newClient = clientRepository.save(client);
        return new ResponseEntity<Client>(newClient, HttpStatus.OK);
    }
}