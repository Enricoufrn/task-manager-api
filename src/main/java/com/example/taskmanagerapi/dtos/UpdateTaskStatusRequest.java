package com.example.taskmanagerapi.dtos;

import com.example.taskmanagerapi.domain.enumerations.TaskStatus;

import java.util.UUID;

public record UpdateTaskStatusRequest(UUID taskId, TaskStatus status) {
}
