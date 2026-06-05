package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BlockchainBlockResponse {
    private Long index;
    private LocalDateTime timestamp;
    private String previousHash;
    private String hash;
    private String data;
}
