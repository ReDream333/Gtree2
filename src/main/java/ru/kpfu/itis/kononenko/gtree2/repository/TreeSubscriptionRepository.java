package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.kononenko.gtree2.entity.TreeSubscription;

import java.util.List;

public interface TreeSubscriptionRepository extends JpaRepository<TreeSubscription, Long> {
    boolean existsByTreeIdAndUserId(Long treeId, Long userId);
    void deleteByTreeIdAndUserId(Long treeId, Long userId);
    List<TreeSubscription> findByTreeId(Long treeId);
}