package com.taskmanager.dto;

import com.taskmanager.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AiGenerateResponse {
    private String description;
    private Priority priority;
    private String estimatedTime;
    private boolean aiGenerated;
}
