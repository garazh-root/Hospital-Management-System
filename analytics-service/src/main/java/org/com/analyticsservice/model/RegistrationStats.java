package org.com.analyticsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.analyticsservice.addtitional.Roles;

import java.util.UUID;

@Entity
@Table(name = "registration_stats")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegistrationStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "total_count", nullable = false)
    private Integer count;
}