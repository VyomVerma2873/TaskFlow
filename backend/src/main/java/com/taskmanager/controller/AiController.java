package com.taskmanager.controller;

import com.taskmanager.dto.AiGenerateRequest;
import com.taskmanager.dto.AiGenerateResponse;
import com.taskmanager.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/generate-task")
    public ResponseEntity<AiGenerateResponse> generateTaskDetails(
            @Valid @RequestBody AiGenerateRequest request) {
        return ResponseEntity.ok(aiService.generateTaskDetails(request.getTitle()));
    }
}
