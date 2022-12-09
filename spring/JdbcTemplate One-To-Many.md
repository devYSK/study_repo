

# JdbcTemplate OneToMany, 1:N 



JdbcTemplate을 이용해서 1:N 상황의 엔티티를 Join하는 방법을 알아보자.

  


먼저 다음과 같은 1:N 관계의 도메인이 있다.

* Order (1)
* OrderItem (N)



```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order {

    private Long orderId;

    private String user;

    private List<OrderItem> items = new ArrayList();

}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {

    private Long itemId;
    private Long orderId;
    private String name;
    private int quantity;

}
```



이 두 도메인을 조인해서 조회해보겠다.



## 1. orderId로 order 1건, orderItem 여러건 조인해서 조회

```java
@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String query = "select * from orders o inner join order_items oi "
        + "on o.order_id = oi.order_id where o.id = :orderId";

    public Optional<Order> findOrderWithItemById(Long orderId) {

        Order findOrder = jdbcTemplate.query(query, new ResultSetExtractor<Order>() {
            @Override
            public Order extractData(ResultSet rs) throws SQLException, DataAccessException {
                Order order = null;
                int row = 0;
                while (rs.next()) {
                    if (order == null) {
                        order = orderMapper.mapRow(rs, row);
                    }

                    if (order != null) {
                        OrderItem orderItem = orderItemMapper.mapRow(rs, row);
                        order.getItems().add(orderItem);
                        row++;
                    }
                }

                return order;
            }
        });

        return Optional.ofNullable(findOrder);
    }

    private final RowMapper<Order> orderMapper = (rs, rowNum) -> {
        Long orderId = rs.getLong("order_id");
        Long userId = rs.getLong("user_id");

        return new Order(orderId, userId);
    };

    private final RowMapper<OrderItem> orderItemMapper = (rs, rowNum) -> {
        Long itemId = rs.getLong("item_id");
        Long orderId = rs.getLong("oi.order_id"); // 조인 시 별칭으로 나옴
        String name = rs.getString("name");
        int quantity = rs.getInt("quantity");

        return new OrderItem(itemId, orderId, name, quantity);
    };

}

```

 

* 두 테이블을 조인하면, 1:N 관계에서는 N만큼의 Row(행)가 나오게 된다.
* Order 1개, OrderItem이 5개 라면 Row는 5개가 나오게 된다
* Order정보는 중복일테니 1개만 만들어 주고, 나머지 Row 는 Count 해가면서 OrderItem을 만들어준다



#### Order

| order_id | user_id |
| -------- | ------- |
| 1        | 99      |

  

#### OrderITem

| item_id | order_id | name | quantity |
| ------- | -------- | ---- | -------- |
| 1       | 1        | 샴푸 | 1        |
| 2       | 1        | 개밥 | 2        |
| 3       | 1        | 개껌 | 3        |
| 4       | 1        | 책   | 4        |
| 5       | 1        | 금   | 5        |



두 테이블을 조인하면

| order_id | user_id | item_id | name | quantity |
| -------- | ------- | ------- | ---- | -------- |
| 1        | 99      | 1       | 샴푸 | 1        |
| 1        | 99      | 2       | 개밥 | 2        |
| 1        | 99      | 3       | 개껌 | 3        |
| 1        | 99      | 4       | 책   | 4        |
| 1        | 99      | 5       | 금   | 5        |



그러므로 Rs를 이용하여 Order가 null이면 한번만 생성하고,  OrderItem을 계속 생성하면서 다음row 카운트를 증가 시킨다



## 2. 모든 Order와 OrderItem을 같이 조회

```java

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Order> findAllOrderWithItems() {
        String query = "select * from orders o inner join order_items oi on o.order_id = oi.order_id";

        List<Order> orders =
            jdbcTemplate.query(query, new ResultSetExtractor<List<Order>>() {
                @Override
                public List<Order> extractData(ResultSet rs)
                    throws SQLException, DataAccessException {
                    List<Order> findOrders = new ArrayList<>();

                    Long orderId = null;
                    Order currentOrder = null;
                    int orderIdx = 0;
                    int itemIdx = 0;

                    while (rs.next()) {
                        if (currentOrder == null || !orderId.equals(rs.getLong("order_id"))) {
                            orderId = rs.getLong("order_id");

                            currentOrder = orderMapper.mapRow(rs, orderIdx++);

                            itemIdx = 0;
                            findOrders.add(currentOrder);
                        }
                        OrderItem orderItem = orderItemMapper.mapRow(rs, itemIdx++);
                        currentOrder.getItems().add(orderItem);
                    }

                    return findOrders;
                }
            });

        return orders.isEmpty() ? new ArrayList<>() : orders;
    }


    private final RowMapper<Order> orderMapper = (rs, rowNum) -> {
        Long orderId = rs.getLong("order_id");
        Long userId = rs.getLong("user_id");

        return new Order(orderId, userId);
    };

    private final RowMapper<OrderItem> orderItemMapper = (rs, rowNum) -> {
        Long itemId = rs.getLong("item_id");
        Long orderId = rs.getLong("oi.order_id"); // 조인 시 별칭으로 나옴
        String name = rs.getString("name");
        int quantity = rs.getInt("quantity");

        return new OrderItem(itemId, orderId, name, quantity);
    };

}
```



* `currentOrder == null || !orderId.equals(rs.getLong("order_id"))` 이부분이 핵심이다.
  * 1:N 처럼 결국에 같은 로우가 계속 나오기 때문에, orderId를 비교하여 새 current Order를 만든다.





# 전체 코드





### 도메인 엔티티

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order {

    private Long orderId;

    private String user;

    private List<OrderItem> items = new ArrayList();

}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {

    private Long itemId;
    private Long orderId;
    private String name;
    private int quantity;

}
```





### JdbcTemplate

```java

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String query = "select * from orders o inner join order_items oi "
        + "on o.order_id = oi.order_id where o.id = :orderId";

    public Optional<Order> findOrderWithItemById(Long orderId) {

        Order findOrder = jdbcTemplate.query(query, new ResultSetExtractor<Order>() {
            @Override
            public Order extractData(ResultSet rs) throws SQLException, DataAccessException {

                Order order = null;
                int row = 0;
                while (rs.next()) {
                    if (order == null) {
                        order = orderMapper.mapRow(rs, row);
                    }

                    if (order != null) {
                        OrderItem orderItem = orderItemMapper.mapRow(rs, row);
                        order.getItems().add(orderItem);
                        row++;
                    }
                }

                return order;
            }
        });

        return Optional.ofNullable(findOrder);
    }


    public List<Order> findAllOrderWithItems() {
        String query = "select * from orders o inner join order_items oi on o.order_id = oi.order_id";

        List<Order> orders =
            jdbcTemplate.query(query, new ResultSetExtractor<List<Order>>() {
                @Override
                public List<Order> extractData(ResultSet rs)
                    throws SQLException, DataAccessException {
                    List<Order> findOrders = new ArrayList<>();

                    Long orderId = null;
                    Order currentOrder = null;
                    int orderIdx = 0;
                    int itemIdx = 0;

                    while (rs.next()) {
                        if (currentOrder == null || !orderId.equals(rs.getLong("order_id"))) {
                            orderId = rs.getLong("order_id");

                            currentOrder = orderMapper.mapRow(rs, orderIdx++);

                            itemIdx = 0;
                            findOrders.add(currentOrder);
                        }
                        OrderItem orderItem = orderItemMapper.mapRow(rs, itemIdx++);
                        currentOrder.getItems().add(orderItem);
                    }

                    return findOrders;
                }
            });

        return orders.isEmpty() ? new ArrayList<>() : orders;
    }


    private final RowMapper<Order> orderMapper = (rs, rowNum) -> {
        Long orderId = rs.getLong("order_id");
        Long userId = rs.getLong("user_id");

        return new Order(orderId, userId);
    };

    private final RowMapper<OrderItem> orderItemMapper = (rs, rowNum) -> {
        Long itemId = rs.getLong("item_id");
        Long orderId = rs.getLong("oi.order_id"); // 조인 시 별칭으로 나옴
        String name = rs.getString("name");
        int quantity = rs.getInt("quantity");

        return new OrderItem(itemId, orderId, name, quantity);
    };

}
```

