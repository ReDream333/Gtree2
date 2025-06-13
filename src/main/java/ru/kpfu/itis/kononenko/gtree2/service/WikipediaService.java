package ru.kpfu.itis.kononenko.gtree2.service;

import java.util.List;

public interface WikipediaService {
    List<String> fetchBiographies(String firstName, String lastName);
}