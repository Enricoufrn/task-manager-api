package com.example.taskmanagerapi.dtos;

import com.example.taskmanagerapi.domain.Task;
import com.example.taskmanagerapi.domain.enumerations.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public class TaskDTO extends AbstractDTO<Task>{
    private UUID id;
    private String title;
    private String description;
    private String status;
    private UserDTO owner;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public TaskDTO() {
    }

    public TaskDTO(Task task){
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus().name();
        this.owner = new UserDTO(task.getOwner());
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Task toEntity() {
        Task task = new Task();
        task.setId(this.id);
        task.setTitle(this.title);
        task.setDescription(this.description);
        if ((this.status == null || this.status.isEmpty()) && this.id == null)
            this.status = TaskStatus.TODO.name();
        task.setStatus(TaskStatus.valueOf(this.status));
        if (this.owner != null && this.id == null)
            task.setOwner(this.owner.toEntity());
        task.setCreatedAt(this.createdAt);
        task.setUpdatedAt(this.updatedAt);
        return task;
    }

    @Override
    public AbstractDTO<Task> fromEntity(Task entity) {
        return new TaskDTO(entity);
    }
}
