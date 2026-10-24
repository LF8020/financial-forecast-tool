package ca.fanshawec.capstone.server.asset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ca.fanshawec.capstone.server.scenario.Scenario;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor 
//LF: Class for Asset, which holds all the data pertaining to each individual financial asset in a scenario.
//    Has a many-to-one relationship from here to Scenario.
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    @Nonnull 
    private Scenario scenario;
    @Nonnull 
    private String name;
    @Nonnull 
    private String description;
    @Nonnull 
    private String worth;
    @Nonnull 
    private String apr;
    @Nonnull 
    private String mortgage;
    @Nonnull 
    private String interest;
}