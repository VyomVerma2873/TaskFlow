package com.taskmanager.repository;

import com.taskmanager.entity.BlockchainBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockchainBlockRepository extends JpaRepository<BlockchainBlock, UUID> {
    List<BlockchainBlock> findAllByOrderByIndexAsc();
    Optional<BlockchainBlock> findTopByOrderByIndexDesc();
    Optional<BlockchainBlock> findByIndex(Long index);
}
