package ru.kpfu.itis.kononenko.gtree2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.entity.TreeSubscription;
import ru.kpfu.itis.kononenko.gtree2.exception.NotFoundException;
import ru.kpfu.itis.kononenko.gtree2.repository.TreeRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.TreeSubscriptionRepository;
import ru.kpfu.itis.kononenko.gtree2.service.TreeSubscriptionService;

@Service
@RequiredArgsConstructor
@Transactional
public class TreeSubscriptionServiceImpl implements TreeSubscriptionService {

    private final TreeSubscriptionRepository subscriptionRepository;
    private final TreeRepository treeRepository;

    @Override
    public void subscribe(Long treeId, Long userId) {
        treeRepository.findById(treeId)
                .orElseThrow(() -> new NotFoundException("Tree not found id=" + treeId));
        if (!subscriptionRepository.existsByTreeIdAndUserId(treeId, userId)) {
            subscriptionRepository.save(TreeSubscription.builder()
                    .treeId(treeId)
                    .userId(userId)
                    .build());
        }
    }

    @Override
    public void unsubscribe(Long treeId, Long userId) {
        subscriptionRepository.deleteByTreeIdAndUserId(treeId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubscribed(Long treeId, Long userId) {
        return subscriptionRepository.existsByTreeIdAndUserId(treeId, userId);
    }
}