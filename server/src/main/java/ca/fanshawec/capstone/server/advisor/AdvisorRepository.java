package ca.fanshawec.capstone.server.advisor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "advisors", path = "advisors")
public interface AdvisorRepository extends CrudRepository<Advisor, Long> {
    @Modifying
    @Transactional
    @Query("DELETE from Advisor WHERE ID = ?1")
    int deleteOne(Long ID);
}