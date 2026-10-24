package ca.fanshawec.capstone.server.scenario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ca.fanshawec.capstone.server.client.Client;
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
//LF: Class for Scenario, which holds all the data pertaining to each collection of assets under each client in the database.
//    Has a many-to-one relationship from its id field to asset and itself uses a many-to-one relationship from itself to client.
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ToString.Exclude
    @ManyToOne
    @Nonnull 
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Nonnull 
    private String name;
    @Nonnull 
    private String description;
}