package hello.jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : ysk
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class Member {

    private String memberId;
    private int money;
}
