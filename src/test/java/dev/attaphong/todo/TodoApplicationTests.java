package dev.attaphong.todo;

import dev.attaphong.todo.controller.ToDoController;
import dev.attaphong.todo.dto.ToDoDTO;
import dev.attaphong.todo.model.Task;
import dev.attaphong.todo.repository.TaskRepository;
import dev.attaphong.todo.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(classes = {TodoApplication.class})
class TodoApplicationTests {
	@Autowired
	ToDoController toDoController;

	@Autowired
	TaskService taskService;

	@Autowired
	TaskRepository taskRepository;

	@Test
	void createToDoThenRead() {
		ToDoDTO toDoDTO = new ToDoDTO();
		String desc = "test";
		toDoDTO.setDescription(desc);
		ResponseEntity<Long> res = toDoController.createToDo(toDoDTO);
		Assertions.assertNotNull(res);
		Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
		Long id = res.getBody();
		Assertions.assertNotNull(id);

		ResponseEntity<Task> resGet = toDoController.getToDo(id);
		Assertions.assertNotNull(resGet);
		Assertions.assertEquals(HttpStatus.OK, resGet.getStatusCode());
		Task task = resGet.getBody();
		Assertions.assertNotNull(task);
		Assertions.assertEquals(id, task.getId());
		Assertions.assertEquals(desc, task.getDescription());
		Assertions.assertFalse(task.isCompleted());
		Assertions.assertFalse(task.isDeleted());

		ResponseEntity<List<Task>> resList = toDoController.getToDoList();
		Assertions.assertNotNull(resList);
		Assertions.assertEquals(HttpStatus.OK, resList.getStatusCode());
		List<Task> list = resList.getBody();
		Assertions.assertNotNull(list);
		Assertions.assertEquals(1, list.size());
		Task actualTask = list.get(0);
		Assertions.assertEquals(id, actualTask.getId());
		Assertions.assertEquals(desc, actualTask.getDescription());
	}

	@Test
	void completeToDo(){
		ToDoDTO toDoDTO = new ToDoDTO();
		String desc = "test";
		toDoDTO.setDescription(desc);
		ResponseEntity<Long> res = toDoController.createToDo(toDoDTO);
		Long id = res.getBody();
		Assertions.assertNotNull(id);

		ResponseEntity<Task> resGet = toDoController.getToDo(id);
		Assertions.assertNotNull(resGet);
		Assertions.assertEquals(HttpStatus.OK, resGet.getStatusCode());
		Task task = resGet.getBody();
		Assertions.assertNotNull(task);
		Assertions.assertEquals(id, task.getId());
		Assertions.assertEquals(desc, task.getDescription());
		Assertions.assertFalse(task.isCompleted());

		toDoController.completeToDo(id);

		resGet = toDoController.getToDo(id);
		Assertions.assertNotNull(resGet);
		Assertions.assertEquals(HttpStatus.OK, resGet.getStatusCode());
		task = resGet.getBody();
		Assertions.assertNotNull(task);
		Assertions.assertTrue(task.isCompleted());
	}

	@Test
	void deleteToDo(){
		ToDoDTO toDoDTO = new ToDoDTO();
		String desc = "test";
		toDoDTO.setDescription(desc);
		ResponseEntity<Long> res = toDoController.createToDo(toDoDTO);
		Long id = res.getBody();
		Assertions.assertNotNull(id);

		ResponseEntity<Task> resGet = toDoController.getToDo(id);
		Assertions.assertNotNull(resGet);
		Assertions.assertEquals(HttpStatus.OK, resGet.getStatusCode());
		Task task = resGet.getBody();
		Assertions.assertNotNull(task);
		Assertions.assertEquals(id, task.getId());
		Assertions.assertEquals(desc, task.getDescription());
		Assertions.assertFalse(task.isDeleted());

		toDoController.deleteToDo(id);

		resGet = toDoController.getToDo(id);
		Assertions.assertNotNull(resGet);
		Assertions.assertEquals(HttpStatus.OK, resGet.getStatusCode());
		task = resGet.getBody();
		Assertions.assertNotNull(task);
		Assertions.assertTrue(task.isDeleted());

		ResponseEntity<List<Task>> resList = toDoController.getToDoList();
		Assertions.assertNotNull(resList);
		Assertions.assertEquals(HttpStatus.OK, resList.getStatusCode());
		List<Task> list = resList.getBody();
		Assertions.assertNotNull(list);
		Assertions.assertEquals(0, list.size());
	}

	@Test
	void completeToDo_whenNotFound(){
		assertThrows(EntityNotFoundException.class,()->{
			ResponseEntity<?> res = toDoController.deleteToDo(10000);
			Assertions.assertNotNull(res);
			Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
		});
	}

}
