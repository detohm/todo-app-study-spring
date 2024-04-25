package dev.attaphong.todo.controller;

import dev.attaphong.todo.dto.ToDoDTO;
import dev.attaphong.todo.model.Task;
import dev.attaphong.todo.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ToDoController {
    private final TaskService taskService;
    public ToDoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Task>> getToDoList(){
        return ResponseEntity.ok(taskService.getToDoList());
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<Task> getToDo(@PathVariable("id") long id){
        return ResponseEntity.of(taskService.getToDo(id));
    }

    @PostMapping("/todo")
    public ResponseEntity<Long> createToDo(@RequestBody ToDoDTO toDoDTO){
        Task task = convertToDoDTOtoTask(toDoDTO);
        return ResponseEntity.ok(taskService.createToDo(task));
    }

    @PutMapping("/todo/{id}/complete")
    public ResponseEntity<?> completeToDo(@PathVariable("id") long id){
        taskService.completeToDo(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteToDo(@PathVariable("id") long id){
        taskService.deleteToDo(id);
        return ResponseEntity.ok().build();
    }

    private static Task convertToDoDTOtoTask(ToDoDTO dto){
        Task task = new Task();
        BeanUtils.copyProperties(dto,task);
        return task;
    }
}
