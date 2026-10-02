package ca.fanshawec.capstone.server.asset;

import ca.fanshawec.capstone.server.preset.Preset;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@RequiredArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "preset_id", nullable = false)
    private Preset preset;
    private String name;
    private String value;
    private String apr;
    private String mortgage;
    private String interest;
}