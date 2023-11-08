package com.example.taskmanagerapi.dtos;

import com.example.taskmanagerapi.domain.enumerations.TaskStatus;

import java.util.List;

public class TasksGroupedDTO {
    private TaskStatus status;
    private List<TaskDTO> tasks;
    private Integer total;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
