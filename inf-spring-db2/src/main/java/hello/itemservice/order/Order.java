package hello.itemservice.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : ysk
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String username; //정상, 예외, 잔고부족

    private String payStatus; //대기, 완료
}