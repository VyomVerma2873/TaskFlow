package com.taskmanager.controller;

import com.taskmanager.dto.BlockchainBlockResponse;
import com.taskmanager.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;

    @GetMapping("/chain")
    public ResponseEntity<List<BlockchainBlockResponse>> getChain() {
        return ResponseEntity.ok(blockchainService.getChain());
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateChain() {
        boolean isValid = blockchainService.validateChain();
        return ResponseEntity.ok(Map.of(
                "valid", isValid,
                "message", isValid ? "Blockchain is valid and intact" : "Blockchain integrity compromised"
        ));
    }
}
