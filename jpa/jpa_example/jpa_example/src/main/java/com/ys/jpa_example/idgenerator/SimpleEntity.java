package com.ys.jpa_example.idgenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@Entity
@Getter
public class SimpleEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String title;

    public SimpleEntity(String title) {
        this.title = title;
    }

    public static SimpleEntity generate(String title) {
        return new SimpleEntity(title);
    }

}
