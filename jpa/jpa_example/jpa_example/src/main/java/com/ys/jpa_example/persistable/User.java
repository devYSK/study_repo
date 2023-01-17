package com.ys.jpa_example.persistable;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

@Entity
public class User implements Persistable<String> {

    @Id
    @GeneratedValue
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }

}
