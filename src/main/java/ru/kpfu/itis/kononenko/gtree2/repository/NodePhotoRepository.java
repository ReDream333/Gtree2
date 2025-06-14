package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.kononenko.gtree2.entity.NodePhoto;

import java.util.List;

public interface NodePhotoRepository extends JpaRepository<NodePhoto, Long> {
    List<NodePhoto> findByNodeId(Long nodeId);
    void deleteAllByNodeId(Long nodeId);
}