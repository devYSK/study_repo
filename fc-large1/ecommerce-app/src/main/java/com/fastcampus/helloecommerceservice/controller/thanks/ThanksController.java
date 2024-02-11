package com.fastcampus.helloecommerceservice.controller.thanks;

import com.fastcampus.helloecommerceservice.domain.customer.CustomerDetail;
import com.fastcampus.helloecommerceservice.domain.order.OrderSheet;
import com.fastcampus.helloecommerceservice.exception.NotFoundOrderSheetException;
import com.fastcampus.helloecommerceservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ThanksController {

    private final OrderService orderService;

    @GetMapping("/thanks")
    public String thanks(@AuthenticationPrincipal CustomerDetail customerDetail, Model model) {
        log.info(">>> 감사페이지, {}", customerDetail);
        Object objectOrderId = model.getAttribute("orderId");
        if (Objects.isNull(objectOrderId)) {
            throw new IllegalArgumentException("주문서 Id를 없습니다.");
        }
        Optional<OrderSheet> optionalOrderSheet = this.orderService.createOrderSheet((Long)objectOrderId);
        if (optionalOrderSheet.isEmpty()) {
            throw new NotFoundOrderSheetException("주문서 정보를 찾을 수 없습니다.");
        }

        OrderSheet orderSheet = optionalOrderSheet.get();
        model.addAttribute("orderSheet", orderSheet);

        log.info(">>> 주문 완료, 감사페이지, {}, {}", customerDetail.getUsername(), orderSheet);
        return "/thanks";
    }
}
