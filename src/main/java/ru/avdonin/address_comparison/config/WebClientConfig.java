package ru.avdonin.address_comparison.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${app.yandex.url}")
    private String yandexUrl;

    @Value("${app.yandex.api-key}")
    public String yandexApiKey;

    @Value("${app.dadata.url}")
    private String dadataUrl;

    @Value("${app.dadata.api-key}")
    private String dadataApiKey;

    @Value("${app.dadata.secret-key}")
    private String dadataSecretKey;

    @Bean
    public WebClient yandexWebClient() {
        return WebClient.builder()
                .baseUrl(yandexUrl + "?apikey=" + yandexApiKey)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient dadataWebClient() {
        return WebClient.builder()
                .baseUrl(dadataUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Token " + dadataApiKey)
                .defaultHeader("X-Secret", dadataSecretKey)
                .build();
    }
}
