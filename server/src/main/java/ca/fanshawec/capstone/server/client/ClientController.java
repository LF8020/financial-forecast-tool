package ca.fanshawec.capstone.server.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ca.fanshawec.capstone.server.exception.DuplicateEntryException;
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
        //LF: TODO: Decrypt request
        Client databaseClient = clientRepository.findByLogin(client.getEmail(), client.getPassword()).orElseThrow(() -> new ItemNotFoundException("Email or password not found in database: " + client.getEmail()));
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<Client>(databaseClient.getReturnClient(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Client> updateOne(@Validated({ClientRequest.FullCheck.class}) @RequestBody ClientRequest client) {//LF: Likely needs input sanitization and validation
        //LF: TODO: Decrypt request
        Client clientToUpdate = clientRepository.findById(client.getId()).orElseThrow(() -> new ItemNotFoundException("Invalid client ID: " + client.getId()));
        if(client.getPassword() == clientToUpdate.getPassword()){
            //LF: TODO: Encrypt before storage
            Client updatedClient = clientRepository.save(new Client(clientToUpdate.getId(), clientToUpdate.getAdvisor(), client.getName(), client.getEmail(), client.getPhone(), client.getSecondPassword()));
            //LF: TODO: Encrypt reponse
            return new ResponseEntity<Client>(updatedClient.getReturnClient(), HttpStatus.OK);
        }
        throw new UnauthorizedException("Invalid password");
    }

    @DeleteMapping("/api/clients/delete")
    public ResponseEntity<Boolean> deleteOne(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest client) {//LF: Needs a way to delete scenarios and assets associated with the client.
        //LF: TODO: Decrypt request
        Client clientToDelete = clientRepository.findById(client.getId()).orElseThrow(() -> new ItemNotFoundException("Invalid client ID: " + client.getId()));
        boolean deleted = false;
        if (clientToDelete.getPassword() == client.getPassword()){
            deleted = clientRepository.deleteOne(clientToDelete.getId());
            //LF: TODO: Encrypt reponse
            return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
        }
        throw new UnauthorizedException("Invalid password");
    }

    @PostMapping("/api/clients")
    public ResponseEntity<Client> createOne(@Validated({ClientRequest.RegisterCheck.class}) @RequestBody ClientRequest client) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findDuplicateEmail(client.getEmail()).or(() -> clientRepository.findDuplicateName(client.getName())).orElse(null);
        if(clientCheck == null){
            //LF: TODO: Encrypt before storage
            clientCheck = clientRepository.save(clientCheck);
            //LF: TODO: Encrypt reponse
            return new ResponseEntity<Client>(clientCheck.getReturnClient(), HttpStatus.OK);
        }
        String message = "Client already exists with provided values: ";
        if(client.getEmail() == clientCheck.getEmail()){
            message += "email: " + client.getEmail();
        }
        if(client.getName() == clientCheck.getName()){
            message += "password: " + client.getName();
        }
        throw new DuplicateEntryException(message);
    }

    @PostMapping("/api/clients/get")
    public ResponseEntity<List<Client>> getClients(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest client) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findById(client.getId()).orElseThrow(() -> new ItemNotFoundException("Invalid client Id: " + client.getId()));
        if(clientCheck.getPassword() != client.getPassword()){
            throw new UnauthorizedException("Invalid password");
        }
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<List<Client>>(clientRepository.findAdvisorClients(clientCheck.getId()).orElse(Collections.emptyList()), HttpStatus.OK);
    }
}