package ru.kpfu.itis.kononenko.gtree2.service;


public interface NodeBiographyService {
    String getBiography(Long nodeId);
    void saveBiography(Long nodeId, String biography);
    void deleteBiography(Long nodeId);
}