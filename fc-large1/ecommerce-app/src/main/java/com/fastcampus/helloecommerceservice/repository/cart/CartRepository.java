package com.fastcampus.helloecommerceservice.repository.cart;

import com.fastcampus.helloecommerceservice.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerIdAndIsDeletedIsFalse(Long customerId);
}
