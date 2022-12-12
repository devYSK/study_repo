package com.ys.rest_docs_boot2.domain.order;

import com.ys.rest_docs_boot2.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderSaveTest {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;


}