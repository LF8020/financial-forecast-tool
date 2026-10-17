package ca.fanshawec.capstone.server.client;
import ca.fanshawec.capstone.server.advisor.Advisor;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@RequiredArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = true)
    private Advisor advisor;
    private String name;
    private String email;
    private String phone;
    private String password;
}