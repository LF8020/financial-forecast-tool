package ca.fanshawec.capstone.server.asset;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ca.fanshawec.capstone.server.scenario.Scenario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@RequiredArgsConstructor
public class FinancialAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;
    private String name;
    private String description;
    // private String value;
    private String apr;
    private String mortgage;
    private String interest;
}