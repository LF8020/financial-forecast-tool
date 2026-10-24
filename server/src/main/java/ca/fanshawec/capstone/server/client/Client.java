package ca.fanshawec.capstone.server.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    public Client(long id, Client advisor, String name, String email, String phone){
        this.id = id;
        this.advisor = advisor;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public Client(long id, String name, String email, String phone){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public Client(long id, String name, String email, String phone, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
    @JsonIgnore 
    public Client getReturnAdvisor(){
        if(advisor == null) return null;
        return new Client(advisor.id, advisor.name, advisor.email, advisor.phone);
    }
    @JsonIgnore
    public Client getReturnClient(){
        return new Client(id, getReturnAdvisor(), name, email, phone);
    }
}