package ca.fanshawec.capstone.server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients")
    public ResponseEntity<Iterable<Client>> findAll() {
        Iterable<Client> clients = clientRepository.findAll();
        return new ResponseEntity<Iterable<Client>>(clients, HttpStatus.OK);
    }
}