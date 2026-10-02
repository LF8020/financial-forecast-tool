package ca.fanshawec.capstone.server.client;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Modifying
    @Transactional
    @Query("DELETE from Client WHERE ID = ?1")
    int deleteOne(Long ID);
}