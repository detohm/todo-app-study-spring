package dev.attaphong.todo.controller;

import dev.attaphong.todo.dto.ToDoDTO;
import dev.attaphong.todo.model.Task;
import dev.attaphong.todo.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ToDoControllerTests {
    private ToDoController toDoController;
    @Mock
    private TaskService taskService;

    @BeforeEach
    public void setup(){
        toDoController = new ToDoController(taskService);
    }

    @Test
    public void getToDoList_whenEmpty(){
        List<Task> list = new ArrayList<>();
        when(taskService.getToDoList()).thenReturn(list);
        ResponseEntity<List<Task>> res = toDoController.getToDoList();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertIterableEquals(list, res.getBody());
    }

    @Test
    public void getToDoList_whenExist(){
        List<Task> list = new ArrayList<>(List.of(new Task(), new Task()));
        when(taskService.getToDoList()).thenReturn(list);
        ResponseEntity<List<Task>> res = toDoController.getToDoList();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertIterableEquals(list, res.getBody());
    }

    @Test
    public void createToDo_whenNormal(){
        long taskId = 1L;
        when(taskService.createToDo(any())).thenReturn(taskId);
        ToDoDTO dto = new ToDoDTO();
        dto.setDescription("test desc");
        ResponseEntity<Long> res = toDoController.createToDo(dto);
        verify(taskService, times(1)).createToDo(any());
        Assertions.assertNotNull(res);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(taskId, res.getBody());
    }

    @Test
    public void completeToDo_whenNormal(){
        long taskId = 1L;
        ResponseEntity<?> res = toDoController.completeToDo(taskId);
        verify(taskService, times(1)).completeToDo(taskId);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void deleteToDo_whenNormal(){
        long taskId = 1L;
        ResponseEntity<?> res = toDoController.deleteToDo(taskId);
        verify(taskService, times(1)).deleteToDo(taskId);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }
}
