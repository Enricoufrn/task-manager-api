package com.example.taskmanagerapi.controllers;

import com.example.taskmanagerapi.dtos.TaskDTO;
import com.example.taskmanagerapi.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin(origins = "*")
public class TaskController extends GenericController{
    private final TaskService taskService;

    public TaskController(TaskService taskService, UriBuilder uriBuilder) {
        super(uriBuilder);
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks(){
        List<TaskDTO> tasks = this.taskService.findAll();
        return tasks;
    }

    @GetMapping(ID)
    public ResponseEntity<?> getTaskById(@PathVariable UUID id){
        TaskDTO task = this.taskService.findTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<?> saveTask(@RequestBody TaskDTO taskDTO){
        TaskDTO task = this.taskService.save(taskDTO);
        URI uri = this.uriBuilder.replacePath(this.getByIdPath()).build(task.getId());
        return ResponseEntity.created(uri).body(task);
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO taskDTO){
        TaskDTO task = this.taskService.save(taskDTO);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping(ID)
    public ResponseEntity<?> deleteTask(@PathVariable UUID id){
        this.taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
