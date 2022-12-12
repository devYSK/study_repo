package com.ys.rest_docs_boot2.domain.member.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class MemberCreateRequest {

    @NotBlank @Size(min = 2, message = "이름은 최소 2글자여야 합니다.")
    private String name;

    @NotBlank @Size(min = 2, message = "닉네임은 최소 2글자여야 합니다.")
    private String nickName;

    @Size(min = 1)
    private int age;

    @NotBlank @Size(min = 1, max = 255)
    private String address;

}
