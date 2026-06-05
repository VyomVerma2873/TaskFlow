package com.taskmanager.service;

import com.taskmanager.dto.BlockchainBlockResponse;
import com.taskmanager.entity.BlockchainBlock;
import com.taskmanager.repository.BlockchainBlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final BlockchainBlockRepository blockchainBlockRepository;

    /**
     * Initialize the genesis block if the chain is empty.
     */
    private void ensureGenesisBlock() {
        if (blockchainBlockRepository.count() == 0) {
            BlockchainBlock genesis = BlockchainBlock.builder()
                    .index(0L)
                    .timestamp(LocalDateTime.now())
                    .previousHash("0")
                    .data("Genesis Block - Task Manager Blockchain Ledger")
                    .hash(calculateHash(0L, LocalDateTime.now(), "0", "Genesis Block - Task Manager Blockchain Ledger"))
                    .build();
            blockchainBlockRepository.save(genesis);
        }
    }

    /**
     * Record a task event on the blockchain ledger.
     */
    public void recordEvent(String eventType, UUID taskId, String eventData) {
        ensureGenesisBlock();

        BlockchainBlock lastBlock = blockchainBlockRepository.findTopByOrderByIndexDesc()
                .orElseThrow(() -> new RuntimeException("Blockchain not initialized"));

        long newIndex = lastBlock.getIndex() + 1;
        LocalDateTime now = LocalDateTime.now();
        String data = String.format("{\"event\":\"%s\",\"taskId\":\"%s\",\"detail\":\"%s\"}",
                eventType, taskId, eventData.replace("\"", "\\\""));

        String newHash = calculateHash(newIndex, now, lastBlock.getHash(), data);

        BlockchainBlock newBlock = BlockchainBlock.builder()
                .index(newIndex)
                .timestamp(now)
                .previousHash(lastBlock.getHash())
                .data(data)
                .hash(newHash)
                .taskId(taskId)
                .build();

        blockchainBlockRepository.save(newBlock);
        log.info("Blockchain event recorded: {} for task {}", eventType, taskId);
    }

    /**
     * Get the full blockchain.
     */
    public List<BlockchainBlockResponse> getChain() {
        ensureGenesisBlock();
        return blockchainBlockRepository.findAllByOrderByIndexAsc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Validate the integrity of the blockchain.
     */
    public boolean validateChain() {
        ensureGenesisBlock();
        List<BlockchainBlock> chain = blockchainBlockRepository.findAllByOrderByIndexAsc();

        for (int i = 1; i < chain.size(); i++) {
            BlockchainBlock current = chain.get(i);
            BlockchainBlock previous = chain.get(i - 1);

            // Check if current block's previousHash matches previous block's hash
            if (!current.getPreviousHash().equals(previous.getHash())) {
                return false;
            }

            // Verify current block's hash
            String recalculatedHash = calculateHash(
                    current.getIndex(),
                    current.getTimestamp(),
                    current.getPreviousHash(),
                    current.getData()
            );
            if (!current.getHash().equals(recalculatedHash)) {
                return false;
            }
        }
        return true;
    }

    private String calculateHash(Long index, LocalDateTime timestamp, String previousHash, String data) {
        String content = index + timestamp.toString() + previousHash + data;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private BlockchainBlockResponse mapToResponse(BlockchainBlock block) {
        return BlockchainBlockResponse.builder()
                .index(block.getIndex())
                .timestamp(block.getTimestamp())
                .previousHash(block.getPreviousHash())
                .hash(block.getHash())
                .data(block.getData())
                .build();
    }
}
