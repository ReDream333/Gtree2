package ru.kpfu.itis.kononenko.gtree2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "node_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    private Long nodeId;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "description")
    private String description;
}
