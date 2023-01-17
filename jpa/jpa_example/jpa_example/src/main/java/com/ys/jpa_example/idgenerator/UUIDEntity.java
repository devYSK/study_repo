package com.ys.jpa_example.idgenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UUIDEntity {

    @Id
    @GeneratedValue
    private String id;

    private String title;

    public UUIDEntity(String title) {
        this.title = title;
    }

}
