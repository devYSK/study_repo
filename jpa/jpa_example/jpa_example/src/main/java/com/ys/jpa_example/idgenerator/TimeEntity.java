package com.ys.jpa_example.idgenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
public class TimeEntity {

    @Id
    @GeneratedValue(generator = "time_uuid_generator")
    @GenericGenerator(
        name = "time_uuid_generator",
        strategy = "com.ys.jpa_example.idgenerator.TimeAndUuidGenerator"
    )
    private String id;

}
