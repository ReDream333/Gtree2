package ru.kpfu.itis.kononenko.gtree2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "conversations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
}