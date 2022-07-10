package com.ys.springbootshop.service;

import com.ys.springbootshop.dto.CartItemDto;
import com.ys.springbootshop.entity.Cart;
import com.ys.springbootshop.entity.CartItem;
import com.ys.springbootshop.entity.Item;
import com.ys.springbootshop.entity.Member;
import com.ys.springbootshop.repository.CartItemRepository;
import com.ys.springbootshop.repository.CartRepository;
import com.ys.springbootshop.repository.ItemRepository;
import com.ys.springbootshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * @author : ysk
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        Optional<Cart> optionalCart = cartRepository.findByMemberId(member.getId());

        Cart cart = optionalCart.orElseGet(() -> {
            Cart createdCart = Cart.createCart(member);
            return cartRepository.save(createdCart);
        });

        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItem.addCount(cartItemDto.getCount());
            return cartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());

            return cartItemRepository.save(cartItem).getId();
        }

    }
}
