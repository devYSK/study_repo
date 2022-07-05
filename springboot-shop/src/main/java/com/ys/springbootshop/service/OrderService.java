package com.ys.springbootshop.service;

import com.ys.springbootshop.dto.OrderDto;
import com.ys.springbootshop.entity.Item;
import com.ys.springbootshop.entity.Member;
import com.ys.springbootshop.entity.Order;
import com.ys.springbootshop.entity.OrderItem;
import com.ys.springbootshop.repository.ItemRepository;
import com.ys.springbootshop.repository.MemberRepository;
import com.ys.springbootshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ysk
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);


        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);

        orderRepository.save(order);

        return order.getId();
    }

}
