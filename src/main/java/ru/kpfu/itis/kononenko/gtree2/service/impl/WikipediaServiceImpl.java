package ru.kpfu.itis.kononenko.gtree2.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.kononenko.gtree2.config.property.WikipediaProperties;
import ru.kpfu.itis.kononenko.gtree2.service.WikipediaService;

import java.io.IOException;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class WikipediaServiceImpl implements WikipediaService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final WikipediaProperties properties;


    @Override
    public List<String> fetchBiographies(String firstName, String lastName) {
        return fetchBiographies(firstName, lastName, "ru");
    }


    public List<String> fetchBiographies(String firstName,
                                         String lastName,
                                         String lang) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name must be provided");
        }
        String titleQuery = firstName + " " + lastName;

        String apiUrl = String.format(properties.getUrl(), lang);
        try {
            // 1) Попытка точного совпадения заголовка
            HttpUrl exactUrl = Objects.requireNonNull(HttpUrl.parse(apiUrl)).newBuilder()
                    .addQueryParameter("action", "query")
                    .addQueryParameter("prop", "extracts")
                    .addQueryParameter("exintro", "1")
                    .addQueryParameter("explaintext", "1")
                    .addQueryParameter("titles", titleQuery)
                    .addQueryParameter("format", "json")
                    .build();
            log.debug("Exact title URL: {}", exactUrl);
            try (Response exactResponse = httpClient.newCall(new Request.Builder().url(exactUrl).build()).execute()) {
                if (exactResponse.isSuccessful() && exactResponse.body() != null) {
                    JsonNode pagesNode = objectMapper.readTree(exactResponse.body().string())
                            .path("query").path("pages");
                    for (Map.Entry<String, JsonNode> entry : iterable(pagesNode.fields())) {
                        JsonNode page = entry.getValue();
                        String extract = page.path("extract").asText();
                        if (!extract.isEmpty()) {
                            return Collections.singletonList(extract);
                        }
                    }
                }
            }

            // 2) search по фразе в заголовке
            HttpUrl genUrl = Objects.requireNonNull(HttpUrl.parse(apiUrl)).newBuilder()
                    .addQueryParameter("action", "query")
                    .addQueryParameter("generator", "search")
                    .addQueryParameter("gsrsearch", '"' + titleQuery + '"')
                    .addQueryParameter("gsrnamespace", "0")
                    .addQueryParameter("gsrlimit", "5")
                    .addQueryParameter("prop", "extracts")
                    .addQueryParameter("exintro", "1")
                    .addQueryParameter("explaintext", "1")
                    .addQueryParameter("format", "json")
                    .build();
            log.debug("Generator search URL: {}", genUrl);
            try (Response genResponse = httpClient.newCall(new Request.Builder().url(genUrl).build()).execute()) {
                if (genResponse.isSuccessful() && genResponse.body() != null) {
                    JsonNode pagesNode = objectMapper.readTree(genResponse.body().string())
                            .path("query").path("pages");
                    List<String> extracts = new ArrayList<>();
                    for (Map.Entry<String, JsonNode> entry : iterable(pagesNode.fields())) {
                        String text = entry.getValue().path("extract").asText();
                        if (!text.isEmpty()) extracts.add(text);
                    }
                    return extracts;
                }
            }

        } catch (IOException e) {
            log.error("Ошибка при запросе к Wikipedia API", e);
        }
        return Collections.emptyList();
    }

    private <T> Iterable<T> iterable(final java.util.Iterator<T> iterator) {
        return () -> iterator;
    }
}