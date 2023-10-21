package com.example.taskmanagerapi.repositories;

import com.example.taskmanagerapi.domain.Task;
import com.example.taskmanagerapi.dtos.TaskFilter;
import org.springframework.data.domain.Page;

public interface ITaskCustomRepository {
    Page<Task> findByFilter(TaskFilter filter);
}
