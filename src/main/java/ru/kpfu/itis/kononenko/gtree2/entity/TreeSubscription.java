package ru.kpfu.itis.kononenko.gtree2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tree_subscriptions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"tree_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tree_id", nullable = false)
    private Long treeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}