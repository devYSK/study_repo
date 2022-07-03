package com.ys.springbootshop.repository;

import com.ys.springbootshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : ysk
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
