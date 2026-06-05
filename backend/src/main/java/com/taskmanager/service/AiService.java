package com.taskmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.AiGenerateResponse;
import com.taskmanager.entity.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate task description, priority, and estimated time from a title using Google Gemini.
     */
    public AiGenerateResponse generateTaskDetails(String title) {
        try {
            String prompt = buildPrompt(title);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    ),
                    "generationConfig", Map.of(
                            "temperature", 0.7,
                            "maxOutputTokens", 512
                    )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            String url = apiUrl + "?key=" + apiKey;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return parseGeminiResponse(response.getBody());
            }
        } catch (Exception e) {
            log.warn("Gemini API call failed, using fallback: {}", e.getMessage());
        }

        // Graceful fallback
        return getFallbackResponse(title);
    }

    private String buildPrompt(String title) {
        return String.format("""
                You are a task management AI assistant. Given the following task title, generate:
                1. A detailed but concise task description (2-3 sentences)
                2. A suggested priority level (LOW, MEDIUM, or HIGH)
                3. An estimated completion time (e.g., "2 hours", "1 day", "30 minutes")

                Respond in the following JSON format only (no markdown, no extra text):
                {
                  "description": "your description here",
                  "priority": "LOW|MEDIUM|HIGH",
                  "estimatedTime": "your estimate here"
                }

                Task Title: "%s"
                """, title);
    }

    private AiGenerateResponse parseGeminiResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // Clean up the response (remove markdown code blocks if present)
            text = text.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();

            JsonNode aiResponse = objectMapper.readTree(text);

            String description = aiResponse.path("description").asText();
            String priorityStr = aiResponse.path("priority").asText().toUpperCase();
            String estimatedTime = aiResponse.path("estimatedTime").asText();

            Priority priority;
            try {
                priority = Priority.valueOf(priorityStr);
            } catch (IllegalArgumentException e) {
                priority = Priority.MEDIUM;
            }

            return AiGenerateResponse.builder()
                    .description(description)
                    .priority(priority)
                    .estimatedTime(estimatedTime)
                    .aiGenerated(true)
                    .build();

        } catch (Exception e) {
            log.warn("Failed to parse Gemini response: {}", e.getMessage());
            return getFallbackResponse("task");
        }
    }

    private AiGenerateResponse getFallbackResponse(String title) {
        return AiGenerateResponse.builder()
                .description("Complete the task: " + title + ". Break it down into smaller steps and track progress.")
                .priority(Priority.MEDIUM)
                .estimatedTime("2 hours")
                .aiGenerated(false)
                .build();
    }
}
