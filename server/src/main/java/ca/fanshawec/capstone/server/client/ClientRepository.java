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

    //LF: Find clients by their id and password or their id and their advisor's password. 
    //    if no client or advisor has both values then it fails the authentication
    @Query("SELECT c FROM Client c WHERE ID = ?1 AND c.password = ?2 OR ID = ?1 AND c.advisor.password = ?2")
    java.util.Optional<Client> findByAuth(Long id, String password);

    //LF: Find clients by their id and password
    //    if no client or advisor has both values then it fails the authentication
    @Query("SELECT c FROM Client c WHERE ID = ?1 AND c.password = ?2")
    java.util.Optional<Client> findByClientAuth(Long id, String password);

    //LF: Find clients by email, this is mainly to check for duplicate emails before adding.
    @Query("SELECT c FROM Client c WHERE c.email = ?1 AND c.name = ?2")
    java.util.Optional<Client> findDuplicate(String email, String name);

    //LF: Find clients by advisorId, this will return a list of all clients with that clientId as their advisor.
    @Query("SELECT c FROM Client c WHERE c.advisor.id = ?1")
    java.util.Optional<List<Client>> findAdvisorClients(long advisorId);

    //LF: Find client advisors by hash, this will return a list any advisor currently using that hash with no duplicates.
    @Query("SELECT c FROM Client c WHERE c.advisor.hash = ?1")
    java.util.Optional<Client> findByHash(String advisorHash);
}