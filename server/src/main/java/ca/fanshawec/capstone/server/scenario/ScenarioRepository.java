package ca.fanshawec.capstone.server.scenario;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "scenarios", path = "scenarios")
public interface ScenarioRepository extends CrudRepository<Scenario, Long> {
    @Modifying
    @Transactional
    @Query("DELETE from Scenario WHERE ID = ?1")
    int deleteOne(Long ID);
}