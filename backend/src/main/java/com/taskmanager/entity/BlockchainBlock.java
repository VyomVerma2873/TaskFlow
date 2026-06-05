package com.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blockchain_ledger")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Long index;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "previous_hash", nullable = false, length = 64)
    private String previousHash;

    @Column(nullable = false, length = 64)
    private String hash;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String data;

    @Column(name = "task_id")
    private UUID taskId;
}
