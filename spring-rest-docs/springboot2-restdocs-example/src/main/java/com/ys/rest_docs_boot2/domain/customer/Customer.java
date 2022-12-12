package com.ys.rest_docs_boot2.domain.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false) @Comment("이름")
    private String firstName;

    @Column(nullable = false) @Comment("성")
    private String lastName;

    public Customer(String firstName, String lastName) {
        validateFirstName(firstName);
        validateLastName(lastName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void changeFirstName(String firstName) {
        validateFirstName(firstName);
        this.firstName = firstName;
    }

    public void changeLastName(String lastName) {
        Assert.isTrue(StringUtils.hasText(lastName), "이름은 빈 값이면 안됩니다.");
        this.lastName = lastName;
    }

    private void validateFirstName(String firstName) {
        Assert.hasText(firstName, "이름은 빈 값이면 안됩니다.");
    }

    private void validateLastName(String lastName) {
        Assert.isTrue(StringUtils.hasText(lastName), "성은 빈 값이면 안됩니다.");
    }

}
