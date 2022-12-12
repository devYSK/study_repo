package com.ys.rest_docs_boot2.domain.member.api;

import com.ys.rest_docs_boot2.domain.member.api.request.MemberCreateRequest;
import com.ys.rest_docs_boot2.domain.member.api.response.MemberCreateResponse;
import com.ys.rest_docs_boot2.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody MemberCreateRequest request) {

        Long memberId = memberService.createMember(request);

        return ResponseEntity.ok(new MemberCreateResponse(memberId));
    }

    @GetMapping
    public ResponseEntity<?> getMember() {
        return ResponseEntity.ok(memberService.findAll());
    }
}
