package ca.fanshawec.capstone.server.asset;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "assets", path = "assets")
public interface AssetRepository extends CrudRepository<Asset, Long> {
    
    //LF: Simple delete query, takes assetId and deletes it from the Asset table
    @Modifying
    @Transactional
    @Query("DELETE from Asset WHERE ID = ?1")
    boolean delete(Long ID);

    //LF: Query to find all assets which have the scenarioId of the input
    @Query("SELECT a from Asset a WHERE a.scenario.id = ?1")
    java.util.Optional<List<AssetReturn>> findAssetsById(Long ID);
}