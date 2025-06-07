package ru.kpfu.itis.kononenko.gtree2.service;


public interface NodeBiographyService {
    String get(Long nodeId);
    void save(Long nodeId, String biography);
    void delete(Long nodeId);
}