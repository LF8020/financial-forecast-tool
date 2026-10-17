package ca.fanshawec.capstone.server.asset;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin 
@RepositoryRestResource(collectionResourceRel = "financial-assets", path = "financial-assets")
public interface FinancialAssetRepository extends CrudRepository<FinancialAsset, Long> {
    @Modifying
    @Transactional
    @Query("DELETE from FinancialAsset WHERE ID = ?1")
    int deleteOne(Long ID);
}