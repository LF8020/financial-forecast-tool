package ca.fanshawec.capstone.server.asset;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "assets", path = "assets")
public interface AssetRepository extends CrudRepository<Asset, Long> {
    @Modifying
    @Transactional
    @Query("DELETE from Asset WHERE ID = ?1")
    int deleteOne(Long ID);
}