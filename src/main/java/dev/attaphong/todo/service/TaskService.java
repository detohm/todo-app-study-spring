package dev.attaphong.todo.service;

import dev.attaphong.todo.model.Task;
import dev.attaphong.todo.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> getToDoList(){
        return taskRepository.findByIsDeletedFalse();
    }

    public Long createToDo(Task task){
        Task savedTask = taskRepository.save(task);
        return savedTask.getId();
    }

    public void completeToDo(long id){
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("id:%d is not found".formatted(id)));
        task.setCompleted(true);
        taskRepository.save(task);
    }

    public void deleteToDo(long id){
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("id:%d is not found".formatted(id)));
        task.setDeleted(true);
        taskRepository.save(task);
    }

    public Optional<Task> getToDo(long id){
        return taskRepository.findById(id);
    }
}
