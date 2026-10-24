package ca.fanshawec.capstone.server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ca.fanshawec.capstone.server.exception.ItemNotFoundException;
import ca.fanshawec.capstone.server.exception.UnauthorizedException;

@CrossOrigin
@RestController
@RequestMapping("/api/clients")

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/api/clients/login")//Login path, password needs to be encrypted before transit and decrypted after.
    public ResponseEntity<Client> findByLogin(@Validated({ClientRequest.LoginCheck.class}) @RequestBody Client client) {//LF: Will likely require the client or advisor managing the client's encrypted password to be sent over as well to prevent bad actors from accessing client data.
        Client databaseClient = clientRepository.findByLogin(client.getEmail(), client.getPassword()).orElseThrow(() -> new ItemNotFoundException("Email or password not found in database: " + client.getEmail()));
        Client databaseAdvisor = null;
        if(client.getAdvisor() != null) {
            databaseAdvisor = client.getAdvisor();
        }
        Client responseAdvisor = (databaseAdvisor == null) ? null : new Client(databaseAdvisor.getId(), databaseAdvisor.getName(), databaseAdvisor.getEmail(), databaseAdvisor.getPhone());
        Client responseClient = new Client(databaseClient.getId(), responseAdvisor, databaseClient.getName(), databaseClient.getEmail(), databaseClient.getPhone());
        return new ResponseEntity<Client>(responseClient, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Client> updateOne(@Validated({ClientRequest.FullCheck.class}) @RequestBody ClientRequest client) {//LF: Likely needs input sanitization and validation
        Client clientToUpdate = clientRepository.findById(client.getId()).orElseThrow(() -> new ItemNotFoundException("Invalid client ID: " + client.getId()));
        if(client.getPassword() == clientToUpdate.getPassword()){
            Client updatedClient = clientRepository.save(new Client(clientToUpdate.getId(), clientToUpdate.getAdvisor(), client.getName(), client.getEmail(), client.getPhone(), client.getSecondPassword()));
            Client responseClient = new Client(updatedClient.getId(), updatedClient.getName(), updatedClient.getEmail(), updatedClient.getPhone());
            return new ResponseEntity<Client>(responseClient, HttpStatus.OK);
        }
        throw new UnauthorizedException("Invalid password");
    }

    @DeleteMapping("/api/clients/delete")
    public ResponseEntity<Boolean> deleteOne(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest client) {//LF: Needs a way to delete scenarios and assets associated with the client.
        Client clientToDelete = clientRepository.findById(client.getId()).orElseThrow(() -> new ItemNotFoundException("Invalid client ID: " + client.getId()));
        boolean deleted = false;
        if (clientToDelete.getPassword() == client.getPassword()){
            deleted = clientRepository.deleteOne(client.getId());
            return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
        }
        throw new UnauthorizedException("Invalid password");
    }

    @PostMapping("/api/clients")
    public ResponseEntity<Client> createOne(@Validated({ClientRequest.RegisterCheck.class}) @RequestBody ClientRequest client) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        Client newClient = new Client(client.getName(), client.getEmail(), client.getPhone(), client.getPassword());
        newClient = clientRepository.save(newClient);
        Client responseClient = new Client(newClient.getId(), newClient.getName(), newClient.getEmail(), newClient.getPhone());
        return new ResponseEntity<Client>(responseClient, HttpStatus.OK);
    }
}