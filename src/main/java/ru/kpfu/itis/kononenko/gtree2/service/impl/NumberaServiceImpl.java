package ru.kpfu.itis.kononenko.gtree2.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.kpfu.itis.kononenko.gtree2.service.NumberaService;

import java.time.LocalDate;
import java.util.Map;

@Service
public class NumberaServiceImpl implements NumberaService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getFact(LocalDate date) {
        if (date == null) {
            return "";
        }
        String url = String.format("http://numbersapi.com/%d/%d/date?json", date.getMonthValue(), date.getDayOfMonth());
        try {
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            Object text = response != null ? response.get("text") : null;
            return text != null ? text.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }
}