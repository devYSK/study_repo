package com.ys.demo.dto;

import com.ys.demo.model.TodoEntity;
import lombok.*;

@ToString
@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class TodoDto {
    private String id;
    private String title;
    private boolean done;

    public TodoDto(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    public static TodoEntity todoEntity(final TodoDto dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }

}
