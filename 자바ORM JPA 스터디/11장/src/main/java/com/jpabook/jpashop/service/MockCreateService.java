package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.item.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MockCreateService {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void initCreateMock() {

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));

        memberService.join(member);

        Book book = createBook("시골개발자의 JPA 책", 20000, 10);
        itemService.saveItem(book);
        itemService.saveItem(createBook("토비의 봄", 40000, 20));

        orderService.order(member.getId(), book.getId(), 5);

    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        return book;
    }

}
