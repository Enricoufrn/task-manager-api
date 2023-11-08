package com.example.taskmanagerapi.repositories;

import com.example.taskmanagerapi.domain.Task;
import com.example.taskmanagerapi.domain.enumerations.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ITaskRepository extends JpaRepository<Task, UUID>, ITaskCustomRepository {
    @Override
    @Query(value = "SELECT * FROM public.task WHERE task.active = true AND task.is_archived = false", nativeQuery = true)
    List<Task> findAll();
    @Query(value = "SELECT * FROM public.task WHERE task.owner_id = ?1 AND task.active = true AND task.is_archived = false", nativeQuery = true)
    List<Task> findAllByOwnerId(UUID ownerId);
    @Query(value = "SELECT * FROM public.task WHERE task.owner_id = ?1 AND task.status = ?2 AND task.active = true AND task.is_archived = false", nativeQuery = true)
    List<Task> findAllByOwnerIdAndStatus(UUID ownerId, String status);
}
