package com.ys.jpa_example.idgenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
public class PrefixEntity {

    @Id
    @GeneratedValue(generator = "hkbks-generator")
    @GenericGenerator(name = "hkbks-generator",
        parameters = @Parameter(name = "prefix", value = "hkbks"),
        strategy = "com.ys.jpa_example.idgenerator.PrefixGenerator")
    private String prodId;

}
