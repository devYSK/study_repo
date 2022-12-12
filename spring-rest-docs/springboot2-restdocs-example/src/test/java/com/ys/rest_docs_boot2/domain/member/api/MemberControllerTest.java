package com.ys.rest_docs_boot2.domain.member.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ys.rest_docs_boot2.domain.member.Member;
import com.ys.rest_docs_boot2.domain.member.service.MemberService;
import com.ys.rest_docs_boot2.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
//@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = {MemberController.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    void postTest() throws Exception {

        given(memberService.findAll())
            .willReturn(createMembers(10));

        this.mockMvc.perform(get("/api/v1/members")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(MockMvcRestDocumentation.document(
                "member-get-all",
                PayloadDocumentation.responseFields(
                    fieldWithPath("[].id").description("member Id"),
                    fieldWithPath("[].name").description("member name"),
                    fieldWithPath("[].nickName").description("nickName"),
                    fieldWithPath("[].age").description("member age"),
                    fieldWithPath("[].address").description("member address"),
                    fieldWithPath("[].createdAt").description("createdAt"),
                    fieldWithPath("[].modifiedAt").description("modifiedAt")
                )

            ));
    }

    private List<Member> createMembers(int size) {

        List<Order> orders = List.of(Order.create("memo"));

        return IntStream.range(0, size)
            .mapToObj((v) -> {
                Member member = new Member("name", "nickName", 28, "nowongu");
//                member.addOrder(Order.create("memo"));
                return member;})

            .collect(Collectors.toList());
    }
}