package com.example.taskmanagerapi.repositories;

import com.example.taskmanagerapi.domain.Task;
import com.example.taskmanagerapi.dtos.TaskFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

@Transactional
public class ITaskCustomRepositoryImpl implements ITaskCustomRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Page<Task> findByFilter(TaskFilter filter) {
        return null;
    }
}
