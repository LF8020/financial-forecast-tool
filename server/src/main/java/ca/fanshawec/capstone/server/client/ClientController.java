package ca.fanshawec.capstone.server.client;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ca.fanshawec.capstone.server.exception.DuplicateEntryException;
import ca.fanshawec.capstone.server.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/clients")
//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.

//LF: Controller for Client. All HTTP paths for client will be handled here.
public class ClientController {
    private final ClientRepository clientRepository;

    //LF: Find an individual client by login information. Explicitly requires:
    /*
        ClientRequest{
            String: email
            String: password
        }
    */
    @PostMapping("/api/clients/login")//Login path, password needs to be encrypted before transit and decrypted after.
    public ResponseEntity<ClientReturn> findByLogin(@Validated({ClientRequest.LoginCheck.class}) @RequestBody ClientRequest clientReq) {//LF: Will likely require the client or advisor managing the client's encrypted password to be sent over as well to prevent bad actors from accessing client data.
        //LF: TODO: Decrypt request
        Client databaseClient = clientRepository.findByLogin(clientReq.getEmail(), clientReq.getPassword()).orElseThrow(() -> new ItemNotFoundException("Email or password not found in database for account: " + clientReq.getEmail()));
        //LF: TODO: Encrypt reponse
        ClientReturn returnClient = new ClientReturn(databaseClient.getId(), (databaseClient.getAdvisor() != null) ? databaseClient.getAdvisor().getId() : null, databaseClient.getName(), databaseClient.getEmail(), databaseClient.getPhone());
        return new ResponseEntity<ClientReturn>(returnClient, HttpStatus.OK);
    }

    //LF: Update a client. Explicitly requires:
    /*
        ClientRequest{
            Long: id
            String: name
            String: email
            String: phone
            String: password
            String: secondPassword (To update password if it's different than the old one)
        }
    */
    @PutMapping
    public ResponseEntity<ClientReturn> updateOne(@Validated({ClientRequest.FullCheck.class}) @RequestBody ClientRequest clientReq) {//LF: Likely needs input sanitization and validation
        //LF: TODO: Decrypt request
        Client clientToUpdate = clientRepository.findByAuth(clientReq.getId(), clientReq.getPassword()).orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        //LF: TODO: Encrypt before storage
        Client updatedClient = clientRepository.save(new Client(clientToUpdate.getId(), clientToUpdate.getAdvisor(), clientReq.getName(), clientReq.getEmail(), clientReq.getPhone(), clientReq.getSecondPassword()));
        //LF: TODO: Encrypt reponse
        ClientReturn returnClient = new ClientReturn(updatedClient.getId(), (updatedClient.getAdvisor() != null) ? updatedClient.getAdvisor().getId() : null, updatedClient.getName(), updatedClient.getEmail(), updatedClient.getPhone());
        return new ResponseEntity<ClientReturn>(returnClient, HttpStatus.OK);
    }

    //LF: Delete a client. Explicitly requires:
    /*
        ClientRequest{
            Long: id
            String: password
        }
    */
    @DeleteMapping("/api/clients/delete")
    public ResponseEntity<Boolean> deleteOne(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {//LF: Needs a way to delete scenarios and assets associated with the client.
        //LF: TODO: Decrypt request
        Client clientToDelete = clientRepository.findByAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        boolean deleted = clientRepository.deleteOne(clientToDelete.getId());;
        //LF: TODO: Cascade delete through scenarios and assets
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    //LF: Create a client. Explicitly requires:
    /*
        ClientRequest{
            String: name
            String: email
            String: phone
            String: password
        }
    */
    @PostMapping("/api/clients")
    public ResponseEntity<ClientReturn> createOne(@Validated({ClientRequest.RegisterCheck.class}) @RequestBody ClientRequest clientReq) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findDuplicateEmail(clientReq.getEmail()).or(() -> clientRepository.findDuplicateName(clientReq.getName())).orElse(null);
        if (clientCheck != null) throw new DuplicateEntryException("Client already exists with one or more of provided values:\n" + clientReq.getName() + "\n" + clientReq.getEmail());
        //LF: TODO: Encrypt before storage
        clientCheck = clientRepository.save(new Client(clientReq.getName(), clientReq.getEmail(), clientReq.getPhone(), clientReq.getPassword()));
        //LF: TODO: Encrypt reponse
        ClientReturn returnClient =  new ClientReturn(clientCheck.getId(), (clientCheck.getAdvisor() != null) ? clientCheck.getAdvisor().getId() : null, clientCheck.getName(), clientCheck.getEmail(), clientCheck.getPhone());
        return new ResponseEntity<ClientReturn>(returnClient, HttpStatus.OK);
    }

    //LF: Get all clients that have this client as the advisor of them. Explicitly requires:
    /*
        ClientRequest{
            Long: id
            String: password
        }
    */
    @PostMapping("/api/clients/get")
    public ResponseEntity<List<Client>> getClients(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid clients. Password should be encrypted in storage and in transit using X25519MLKEM768.
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<List<Client>>(clientRepository.findAdvisorClients(clientCheck.getId())
            .orElse(Collections.emptyList()), HttpStatus.OK);
    }
}