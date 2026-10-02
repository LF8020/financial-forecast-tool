package ca.fanshawec.capstone.server.preset;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ca.fanshawec.capstone.server.client.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@RequiredArgsConstructor
public class Preset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}