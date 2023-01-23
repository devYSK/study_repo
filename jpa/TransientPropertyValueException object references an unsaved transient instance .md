

# TransientPropertyValueException: object references an unsaved transient instance 에러



```
org.hibernate.TransientPropertyValueException: 
object references an unsaved transient instance - save the transient instance before flushing : 
```



연관관계가 있는 엔티티끼리 영속성을 관리할때(영속성 전이) 생기는 오류이다.

FK로 지정하는 엔티티가 아직 영속상태(Persist) 가 되지 않거나,  부모객체에서 자식객체를 한번에 저장하려고할때 발생한다.



## 해결

영속성 전이를 위해 cascade type을 지정한다.   


@OneToMany, @ManyToOne 어노테이션이 존재하는 연관관계의  join하는쪽(연관관계의 주인)에 cascade를 설정.

> cascade = CascadeType.ALL 또는 cascade = CascadeType.PERSIST 

- CascadeType.PERSIST
  - 엔티티를 생성하고, 연관 엔티티를 추가한 상태로 영속화할 때 연관 엔티티도 함께 psersist()가 수행된다.
- CascadeType.MERGE
  - 트랜잭션이 종료되고 detach상태에서 연관 엔티티를 추가하거나 변경된 이후에 부모 엔티티가 merge를 수행하게 되면 변경사항이 연관 엔티티에도 적영된다.
- CascadeType.REFRESH
  - 엔티티를 새로고칠 때, 이 필드에 보유된 엔티티도 새로고친다.
- CascadeType.REMOVE
  - 엔티티를 삭제할 때, 이 필드에 보유된 엔티티도 삭제한다.
- CascadeType.DETACH
  - 부모 엔티티가 detach()를 수행하게 되면, 연관된 엔티티도 detach() 상태가 되어 변경사항이 반영되지 않는다.
- CascadeType.ALL
  - 모든 Cascade 적용



```java
@Entity
public class Parent {
  
  @Id
  private Long id; 
  ...
}

@Entity
public class Child {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
```

