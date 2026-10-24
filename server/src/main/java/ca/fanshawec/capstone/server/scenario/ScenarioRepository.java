package ca.fanshawec.capstone.server.scenario;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "scenarios", path = "scenarios")
public interface ScenarioRepository extends CrudRepository<Scenario, Long> {

    //LF: Simple delete query, takes clientId and deletes it from the Asset table
    @Modifying
    @Transactional
    @Query("DELETE from Scenario WHERE ID = ?1")
    boolean delete(Long ID);

    //LF: Query which returns all scenarios which share the inputted clientId
    @Query("SELECT s FROM Scenario s WHERE s.client.id = ?1")
    java.util.Optional<List<ScenarioReturn>> findScenariosById(long clientID);
    //LF: Query which selects an existing scenario based off of the inputted scenarioId and the authenticaed clientId.
    @Query("SELECT s FROM Scenario s WHERE ID = ?1 AND s.client.id = ?2")
    java.util.Optional<Scenario> findByClient(long id, long clientId);
}