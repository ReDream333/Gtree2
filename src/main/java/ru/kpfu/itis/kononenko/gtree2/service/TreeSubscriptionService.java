package ru.kpfu.itis.kononenko.gtree2.service;

public interface TreeSubscriptionService {
    void subscribe(Long treeId, Long userId);
    void unsubscribe(Long treeId, Long userId);
    boolean isSubscribed(Long treeId, Long userId);
}