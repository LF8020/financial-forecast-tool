package ca.fanshawec.capstone.server.client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends CrudRepository<Client, Long> {
}