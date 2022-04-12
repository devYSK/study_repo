package com.ys.demo.controller;

import com.ys.demo.dto.ResponseDto;
import com.ys.demo.dto.TodoDto;
import com.ys.demo.model.TodoEntity;
import com.ys.demo.serivce.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    private static final String temporaryUserId = "temporary-user";

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDto dto) {
        log.info("Post {}", dto.toString());
        try {
            TodoEntity entity = TodoDto.todoEntity(dto);

            entity.changeId(null);

            entity.changeUserId(temporaryUserId);

            List<TodoDto> dtos = todoService.create(entity).stream()
                    .map(TodoDto::new).collect(Collectors.toList());

            ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                    .data(dtos).build();

            return ResponseEntity.ok().body(build);

        } catch (Exception e) {
            String error = e.getMessage();

            ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                    .error(error).build();

            return ResponseEntity.badRequest().body(build);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        log.info("GET");
        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);

        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                .data(dtos).build();

        return ResponseEntity.ok().body(build);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto dto) {
        log.info("Put {}", dto.toString());
        TodoEntity entity = TodoDto.todoEntity(dto);

        entity.changeId(temporaryUserId);

        List<TodoEntity> entities = todoService.update(entity);

        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                .data(dtos).build();

        return ResponseEntity.ok().body(build);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDto dto) {
        log.info("Delete {}", dto.toString());
        try {
            TodoEntity entity = TodoDto.todoEntity(dto);

            entity.changeUserId(temporaryUserId);

            List<TodoDto> dtos = todoService.create(entity).stream()
                    .map(TodoDto::new).collect(Collectors.toList());

            ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                    .data(dtos).build();

            return ResponseEntity.ok().body(build);

        } catch (Exception e) {
            String error = e.getMessage();

            ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                    .error(error).build();

            return ResponseEntity.badRequest().body(build);
        }
    }

}
