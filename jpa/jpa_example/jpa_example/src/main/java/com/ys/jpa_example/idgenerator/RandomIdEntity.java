package com.ys.jpa_example.idgenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
public class RandomIdEntity {

    @Id
    @GeneratedValue(generator = "randmark")
    @GenericGenerator(
        name = "randmark",
        strategy = "com.ys.jpa_example.idgenerator.RandomIdGenerator"
    )
    private Long id;

}
