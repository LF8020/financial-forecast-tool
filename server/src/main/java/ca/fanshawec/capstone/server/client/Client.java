package ca.fanshawec.capstone.server.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor 
@RequiredArgsConstructor
@AllArgsConstructor 
//LF: Class for Client, which holds all the data pertaining to each individual financial client in the database.
//    Has a many-to-one relationship from 'advisor' field to itself in the ID column.
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = true)
    private Client advisor;
    @Nonnull
    private String name;
    @Nonnull 
    private String email;
    @Nonnull 
    private String phone;
    @Nonnull 
    private String password;
}