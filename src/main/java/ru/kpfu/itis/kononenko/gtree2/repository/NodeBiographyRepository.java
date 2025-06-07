package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.kononenko.gtree2.entity.NodeBiography;

import java.util.Optional;

public interface NodeBiographyRepository extends JpaRepository<NodeBiography, Long> {
    Optional<NodeBiography> findByNodeId(Long nodeId);
    void deleteByNodeId(Long nodeId);
}