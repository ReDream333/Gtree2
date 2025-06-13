package ru.kpfu.itis.kononenko.gtree2.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.kpfu.itis.kononenko.gtree2.service.NumberaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class NumberaServiceImpl implements NumberaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getFact(LocalDate date) {
        if (date == null) {
            return "";
        }
        String url = "http://numbersapi.com/%d/%d/date?json".formatted(
                date.getMonthValue(),
                date.getDayOfMonth());
        try {
            String json = restTemplate.getForObject(url, String.class);
            if (json == null || json.isBlank()) {
                return "";
            }
            JsonNode factNode = mapper.readTree(json);
            String text = factNode.path("text").asText("");
            if (text.isBlank()) {
                return "";
            }

            String encoded = java.net.URLEncoder.encode(text, StandardCharsets.UTF_8);
            String trUrl = "https://api.mymemory.translated.net/get?q=" + encoded + "&langpair=en|ru";
            String trJson = restTemplate.getForObject(trUrl, String.class);

            if (trJson != null && !trJson.isBlank()) {
                JsonNode trNode = mapper.readTree(trJson);
                JsonNode data = trNode.path("responseData");
                if (data.has("translatedText")) {
                    String raw = data.get("translatedText").asText();
                    return URLDecoder.decode(raw, StandardCharsets.UTF_8);
                }
            }

            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}