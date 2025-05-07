package com.c208.sleephony.global.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GptClient {

    private final WebClient webClient;

    public GptClient(@Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> getAdvice(String prompt) {
        List<Message> msgs = List.of(
                new Message("system",
                        "You are a sleep coach. Give concise, actionable sleep advice."),
                new Message("user", prompt)
        );

        RequestBody body = new RequestBody("gpt-4o-mini", msgs, 0.8);

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ResponseBody.class)
                .map(r -> r.choices.get(0).message.content.trim());
    }

    /* DTO */
    record Message(String role, String content) {}
    record RequestBody(String model, List<Message> messages, Double temperature) {}
    @Getter static class ResponseBody { List<Choice> choices; @Getter static class Choice { Message message; } }
}
