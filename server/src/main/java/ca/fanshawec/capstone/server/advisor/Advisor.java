package ca.fanshawec.capstone.server.advisor;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//LF: Could possibly remove this in the future with a few modifications to client, even possibly adding a table for a many-to-many relationship between clients acting as advisors for other clients instead, which would simplify the database's structure.
@Entity
@Data
@RequiredArgsConstructor
public class Advisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String phone;
    private String password;
}