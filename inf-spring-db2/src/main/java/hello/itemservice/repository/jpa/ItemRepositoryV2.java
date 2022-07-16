package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : ysk
 */
public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}