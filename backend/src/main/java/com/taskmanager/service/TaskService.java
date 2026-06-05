package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.exception.BadRequestException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final BlockchainService blockchainService;

    public TaskResponse createTask(TaskRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .status(TaskStatus.TODO)
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);

        // Record blockchain event
        blockchainService.recordEvent("TASK_CREATED", savedTask.getId(),
                "Task created: " + savedTask.getTitle());

        return mapToResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks(String userEmail) {
        User user = getUserByEmail(userEmail);
        return taskRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(UUID taskId, String userEmail) {
        Task task = getTaskAndVerifyOwnership(taskId, userEmail);
        return mapToResponse(task);
    }

    public TaskResponse updateTask(UUID taskId, TaskRequest request, String userEmail) {
        Task task = getTaskAndVerifyOwnership(taskId, userEmail);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updatedTask = taskRepository.save(task);

        blockchainService.recordEvent("TASK_UPDATED", updatedTask.getId(),
                "Task updated: " + updatedTask.getTitle());

        return mapToResponse(updatedTask);
    }

    public void deleteTask(UUID taskId, String userEmail) {
        Task task = getTaskAndVerifyOwnership(taskId, userEmail);

        blockchainService.recordEvent("TASK_DELETED", task.getId(),
                "Task deleted: " + task.getTitle());

        taskRepository.delete(task);
    }

    public TaskResponse updateTaskStatus(UUID taskId, TaskStatus status, String userEmail) {
        Task task = getTaskAndVerifyOwnership(taskId, userEmail);

        TaskStatus oldStatus = task.getStatus();
        task.setStatus(status);

        Task updatedTask = taskRepository.save(task);

        blockchainService.recordEvent("STATUS_CHANGED", updatedTask.getId(),
                "Status changed from " + oldStatus + " to " + status + " for task: " + updatedTask.getTitle());

        return mapToResponse(updatedTask);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Task getTaskAndVerifyOwnership(UUID taskId, String userEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (!task.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You do not have permission to access this task");
        }

        return task;
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
