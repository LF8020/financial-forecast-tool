package ca.fanshawec.capstone.server.client;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends CrudRepository<Client, Long> {

    //LF: Simple delete query, takes clientId and deletes it from the Asset table
    @Modifying
    @Transactional
    @Query("DELETE from Client WHERE ID = ?1")
    boolean deleteOne(Long ID);

    //LF: Find clients by email and password login. May need to be updated for encryption but maybe hashes passwords can be compared just fine?
    @Query("SELECT c FROM Client c WHERE c.email = ?1 AND c.password = ?2")
    java.util.Optional<Client> findByLogin(String email, String password);

    //LF: Find clients by their auth. Inputs id and password and checks it against all clients, 
    //    if no client has both values then it fails the authentication
    @Query("SELECT c FROM Client c WHERE ID = ?1 AND c.password = ?2")
    java.util.Optional<Client> findByAuth(Long id, String password);

    //LF: Find clients by email, this is mainly to check for duplicate emails before adding.
    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    java.util.Optional<Client> findDuplicateEmail(String email);

    //LF: Find clients by name, this is mainly to check for duplicate names before adding.
    @Query("SELECT c FROM Client c WHERE c.name = ?1")
    java.util.Optional<Client> findDuplicateName(String name);

    //LF: Find clients by advisorId, this will return a list of all clients with that clientId as their advisor.
    @Query("SELECT c FROM Client c WHERE c.advisor.id = ?1")
    java.util.Optional<List<Client>> findAdvisorClients(long advisorId);

    //LF: Find client advisors by hash, this will return a list any advisor currently using that hash with no duplicates.
    @Query("SELECT c FROM Client c WHERE c.advisor.hash = ?1")
    java.util.Optional<Client> findByHash(String advisorHash);
}