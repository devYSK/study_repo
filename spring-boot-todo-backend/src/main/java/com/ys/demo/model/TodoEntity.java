package com.ys.demo.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name = "Todo")
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @ToString
public class TodoEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String userId;
    private String title;
    private boolean done;

    public void changeId(String id) {
        this.id = id;
    }

    public void changeUserId(String userId) {
        this.userId = userId;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDone(boolean done) {
        this.done = done;
    }

}
