package com.example.taskmanagerapi.services;

import com.example.taskmanagerapi.domain.Task;
import com.example.taskmanagerapi.domain.User;
import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import com.example.taskmanagerapi.domain.enumerations.TaskStatus;
import com.example.taskmanagerapi.dtos.TaskDTO;
import com.example.taskmanagerapi.dtos.TasksGroupedDTO;
import com.example.taskmanagerapi.dtos.UpdateTaskStatusRequest;
import com.example.taskmanagerapi.exceptions.ResourceException;
import com.example.taskmanagerapi.helper.MessageHelper;
import com.example.taskmanagerapi.helper.UserHelper;
import com.example.taskmanagerapi.repositories.ITaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TaskService {
    private final ITaskRepository taskRepository;
    private final MessageHelper messageHelper;
    private final UserHelper userHelper;

    public TaskService(ITaskRepository taskRepository,
                       MessageHelper messageHelper,
                       UserHelper userHelper) {
        this.taskRepository = taskRepository;
        this.messageHelper = messageHelper;
        this.userHelper = userHelper;
    }
    public TaskDTO save(TaskDTO taskDTO){
        Task task = taskDTO.toEntity();
        User owner = this.userHelper.getLoggedUser();
        task.setOwner(owner);
        task = this.save(task);
        return new TaskDTO(task);
    }
    private Task save(Task task){
        return this.taskRepository.save(task);
    }
    public List<TaskDTO> findAll(){
        User loggedUser = this.userHelper.getLoggedUser();
        return this.taskRepository.findAllByOwnerId(loggedUser.getId()).stream().map(TaskDTO::new).toList();
    }
    public List<TaskDTO> findAllByStatus(TaskStatus status){
        User loggedUser = this.userHelper.getLoggedUser();
        return this.taskRepository.findAllByOwnerIdAndStatus(loggedUser.getId(), status.name()).stream().map(TaskDTO::new).toList();
    }
    public TaskDTO findTaskById(UUID id){
        Task task = this.findById(id);
        return new TaskDTO(task);
    }
    private Task findById(UUID id){
        if (id == null) throw new ResourceException(this.messageHelper.getMessage(MessageCode.BAD_REQUEST), "id: "+ id, HttpStatus.NOT_FOUND);
        return this.taskRepository.findById(id).orElseThrow(() ->
                new ResourceException(this.messageHelper.getMessage(MessageCode.TASK_NOT_FOUND), "id: "+ id, HttpStatus.NOT_FOUND));
    }

    public void delete(UUID id){
        Task task = this.findById(id);
        User loggedUser = this.userHelper.getLoggedUser();
        if (!task.getOwner().getId().equals(loggedUser.getId()))
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.PERMISSION_DENIED), "", HttpStatus.FORBIDDEN);
        this.taskRepository.delete(task);
    }

    public TaskDTO changeTaskStatus(UpdateTaskStatusRequest request){
        UUID taskId = request.taskId();
        TaskStatus status = request.status();
        Task task = this.findById(taskId);
        User loggedUser = this.userHelper.getLoggedUser();
        if (!task.getOwner().getId().equals(loggedUser.getId()))
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.PERMISSION_DENIED), "", HttpStatus.FORBIDDEN);
        if (status == null) throw new ResourceException(this.messageHelper.getMessage(MessageCode.BAD_REQUEST), "status: "+ status, HttpStatus.BAD_REQUEST);
        TaskStatus oldStatus = task.getStatus();
        if (oldStatus == TaskStatus.ARCHIVED)
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.TASK_ARCHIVED), "", HttpStatus.BAD_REQUEST);
        if (oldStatus == TaskStatus.DONE && status != TaskStatus.ARCHIVED)
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.CHANGE_STATUS_TASK_NOT_ALLOWED), "", HttpStatus.BAD_REQUEST);
        task.setStatus(status);
        return new TaskDTO(this.save(task));
    }

    public List<TasksGroupedDTO> getTasksGroupedByStatus(){
        List<TasksGroupedDTO> tasksGrouped = new ArrayList<>();
        Arrays.stream(TaskStatus.values()).forEach(status -> {
            List<TaskDTO> tasks = this.findAllByStatus(status);
            TasksGroupedDTO tasksGroupedDTO = new TasksGroupedDTO();
            tasksGroupedDTO.setStatus(status);
            tasksGroupedDTO.setTasks(tasks);
            tasksGroupedDTO.setTotal(tasks.size());
            tasksGrouped.add(tasksGroupedDTO);
        });
        return tasksGrouped;
    }
}
