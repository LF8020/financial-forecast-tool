package ca.fanshawec.capstone.server.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/assets")

//LF: All input should be sanitized and validated to prevent actions by malicious actors, especially SQL injection attacks.
//    Additionally, encryption should be used for not only client passwords but also other sensitive data such as the financial information contained in scenarios and assets.
//    All sensitive information will be encrypted at rest and in transit using X25519MLKEM768, a post-quantum encryption algorithm.
public class FinancialAssetController {
    @Autowired
    private FinancialAssetRepository assetRepository;

    @GetMapping
    public ResponseEntity<Iterable<FinancialAsset>> findAll() {//LF: Likely will be removed in the future.
        Iterable<FinancialAsset> assets = assetRepository.findAll();
        return new ResponseEntity<Iterable<FinancialAsset>>(assets, HttpStatus.OK);
    }
    @GetMapping("/api/assets/{id}")
    public ResponseEntity<FinancialAsset> find(@PathVariable long id) {//LF: Will likely require the client or advisor managing the client's encrypted password to be sent over as well to prevent bad actors from accessing asset data.
        FinancialAsset asset = assetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid asset ID: " + id));
        return new ResponseEntity<FinancialAsset>(asset, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<FinancialAsset> updateOne(@RequestBody FinancialAsset asset) {//LF: Likely needs input sanitization and validation
        FinancialAsset updatedAsset = assetRepository.save(asset);
        return new ResponseEntity<FinancialAsset>(updatedAsset, HttpStatus.OK);
    }
    @DeleteMapping("/api/assets/{id}")
    public ResponseEntity<Integer> deleteOne(@PathVariable long id) {//LF: Likely needs some sort of validation to prevent malicious deletions by bad actors.
        int deletedCount = assetRepository.deleteOne(id);
        return new ResponseEntity<Integer>(deletedCount, HttpStatus.OK);
    }
    @PostMapping("/api/assets")
    public ResponseEntity<FinancialAsset> createOne(@RequestBody FinancialAsset asset) {//LF: Input validation and sanitization is needed here to prevent bad actors from creating invalid assets. Password should be encrypted using X25519MLKEM768.
        FinancialAsset newAsset = assetRepository.save(asset);
        return new ResponseEntity<FinancialAsset>(newAsset, HttpStatus.OK);
    }
}