package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Task> findByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, TaskStatus status);
    long countByUserIdAndStatus(UUID userId, TaskStatus status);
    long countByUserId(UUID userId);
}
