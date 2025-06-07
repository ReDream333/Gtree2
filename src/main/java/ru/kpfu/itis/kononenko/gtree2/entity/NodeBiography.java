package ru.kpfu.itis.kononenko.gtree2.entity;

import lombok.Builder;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nodes_biography")
@Data
@NoArgsConstructor
public class NodeBiography {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false, unique = true)
    private Long nodeId;

    @Column(columnDefinition = "TEXT")
    private String biography;
}
