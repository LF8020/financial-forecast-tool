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
import ca.fanshawec.capstone.server.exception.UnauthorizedException;//Deprecated right now, may be used later.
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
    public ResponseEntity<ClientReturn> findByLogin(@Validated({ClientRequest.LoginCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client databaseClient = clientRepository.findByLogin(clientReq.getEmail(), clientReq.getPassword()).orElseThrow(() -> new ItemNotFoundException("Email or password not found in database for account: " + clientReq.getEmail()));
        //LF: TODO: Encrypt reponse
        ClientReturn returnClient = 
        new ClientReturn(
            databaseClient.getId(), 
            (databaseClient.getAdvisor() != null) ? new ClientReturn(
                databaseClient.getAdvisor().getId(), 
                null, 
                databaseClient.getAdvisor().getName(), 
                databaseClient.getAdvisor().getEmail(), 
                databaseClient.getAdvisor().getPhone()
            ) : null, 
            databaseClient.getName(), 
            databaseClient.getEmail(), 
            databaseClient.getPhone()
        );
        return new ResponseEntity<ClientReturn>(returnClient, HttpStatus.OK);
    }

    //LF: Update a client. Explicitly requires:
    /*
        ClientRequest{
            Long: id (clientId)
            String: name
            String: email
            String: phone
            String: password
            String: secondPassword (To update password if it's different than the old one)
        }
    */
    @PutMapping
    public ResponseEntity<ClientReturn> updateOne(@Validated({ClientRequest.FullCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByAuth(clientReq.getId(), clientReq.getPassword()).orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        //LF: TODO: Encrypt before storage
        clientCheck = clientRepository.save(new Client(clientCheck.getId(), clientCheck.getAdvisor(), clientCheck.getHash(), clientReq.getName(), clientReq.getEmail(), clientReq.getPhone(), clientReq.getSecondPassword()));
        //LF: TODO: Encrypt reponse
        ClientReturn returnClient = new ClientReturn(
            clientCheck.getId(), 
            (clientCheck.getAdvisor() != null) ? new ClientReturn(
                clientCheck.getAdvisor().getId(), 
                null, 
                clientCheck.getAdvisor().getName(), 
                clientCheck.getAdvisor().getEmail(), 
                clientCheck.getAdvisor().getPhone()
            ) : null, 
            clientCheck.getName(), 
            clientCheck.getEmail(), 
            clientCheck.getPhone()
        );
        return new ResponseEntity<ClientReturn>(returnClient, HttpStatus.OK);
    }

    //LF: Delete a client, cascades deletion through scenarios and assets and sets 
    //    advisors of clients referencing this one to null. Explicitly requires:
    /*
        ClientRequest{
            Long: id (clientId)
            String: password
        }
    */
    @DeleteMapping("/api/clients/delete")
    public ResponseEntity<Boolean> deleteOne(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientToDelete = clientRepository.findByAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        boolean deleted = clientRepository.deleteOne(clientToDelete.getId());;
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
    public ResponseEntity<ClientReturn> createOne(@Validated({ClientRequest.RegisterCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findDuplicate(clientReq.getEmail(), clientReq.getName())
            .orElseThrow(() -> new DuplicateEntryException("Client already exists with one or more of provided values:\n" + clientReq.getName() + "\n" + clientReq.getEmail()));
        //LF: TODO: Encrypt before storage
        clientCheck = clientRepository.save(new Client(clientReq.getName(), clientReq.getEmail(), clientReq.getPhone(), clientReq.getPassword()));
        //LF: TODO: Encrypt reponse
        ClientReturn returnClient = new ClientReturn(
            clientCheck.getId(), 
            (clientCheck.getAdvisor() != null) ? new ClientReturn(
                clientCheck.getAdvisor().getId(), 
                null, 
                clientCheck.getAdvisor().getName(), 
                clientCheck.getAdvisor().getEmail(), 
                clientCheck.getAdvisor().getPhone()
            ) : null, 
            clientCheck.getName(), 
            clientCheck.getEmail(), 
            clientCheck.getPhone()
        );
        return new ResponseEntity<ClientReturn>(returnClient, HttpStatus.OK);
    }

    //LF: Get all clients that have this client as the advisor of them. Explicitly requires:
    /*
        ClientRequest{
            Long: id
            String: password
        }
    */
    @PostMapping("/api/clients/get-advisor-clients")
    public ResponseEntity<List<Client>> getClients(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByClientAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<List<Client>>(clientRepository.findAdvisorClients(clientCheck.getId())
            .orElse(Collections.emptyList()), HttpStatus.OK);
    }

    //LF: Generate and get a hash from the DB for the purpose of giving to another client for them to add the client assigned the hash as an advisor
    /*
        ClientRequest{
            Long: id
            String: password
        }
    */
    @PostMapping("/api/clients/get-hash")
    public ResponseEntity<String> getHash(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByClientAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        //LF: Generate an advisorHash for this client to give to a client who wishes to add this client as an advisor. The hash is generated using a random UUID.
        String advisorHash = java.util.UUID.randomUUID().toString();//The chance of a collision is astronomically low and this isn't a hash critical to function so this will be fine.
        clientCheck = clientRepository.save(new Client(clientCheck.getId(), clientCheck.getAdvisor(), advisorHash, clientReq.getName(), clientReq.getEmail(), clientReq.getPhone(), clientReq.getSecondPassword()));
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<String>(clientCheck.getHash(), HttpStatus.OK);
    }

    //LF: Remove the hash from a client.
    /*
        ClientRequest{
            Long: id
            String: password
        }
    */
    @PostMapping("/api/clients/remove-hash")
    public ResponseEntity<Boolean> removeHash(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByClientAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        clientCheck = clientRepository.save(new Client(clientCheck.getId(), clientCheck.getAdvisor(), null, clientReq.getName(), clientReq.getEmail(), clientReq.getPhone(), clientReq.getSecondPassword()));
        //LF: TODO: Response encryption likely not needed.
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    //LF: Add an advisor as a client. Uses the ADVISOR client's hash. Client gets this hash off-application via communication with the advisor.
    /*
        ClientRequest{
            Long: id
            String: password
            String: hash
        }
    */
    @PostMapping("/api/clients/add-advisor")
    public ResponseEntity<Client> addAdvisor(@Validated({ClientRequest.AuthCheck.class, ClientRequest.HashCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByClientAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        Client advisorCheck = clientRepository.findByHash(clientReq.getHash())
            .orElseThrow(() -> new ItemNotFoundException("Advisor using hash not found, please ask your financial advisor for another hash."));
        advisorCheck.setHash(null);
        advisorCheck = clientRepository.save(advisorCheck);
        clientCheck.setAdvisor(advisorCheck);
        clientCheck = clientRepository.save(clientCheck);
        
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<Client>(clientCheck, HttpStatus.OK);
    }

    //LF: Remove an advisor as a client or advisor. Maybe can be modified later to support multiple advisors per client if we choose to do so.
    /*
        ClientRequest{
            Long: id (clientId if client calling this is advisor)
            String: password (advisor password if client calling this is advisor)
        }
    */
    @PostMapping("/api/clients/remove-advisor")
    public ResponseEntity<Client> removeAdvisor(@Validated({ClientRequest.AuthCheck.class}) @RequestBody ClientRequest clientReq) {
        //LF: TODO: Decrypt request
        Client clientCheck = clientRepository.findByAuth(clientReq.getId(), clientReq.getPassword())
            .orElseThrow(() -> new ItemNotFoundException("Client not found or password authentication failed. ID: " + clientReq.getId()));
        clientCheck.setAdvisor(null);
        clientCheck = clientRepository.save(clientCheck);
        //LF: TODO: Encrypt reponse
        return new ResponseEntity<Client>(clientCheck, HttpStatus.OK);
    }
}