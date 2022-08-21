package com.ys.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

/**
 * @author : ysk
 */

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnnotationTest {


    @Test
    void test_test_test() {
        System.out.println("ㅎㅇ");
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void EnabledOnOs() {
        System.out.println("운영체제에 따라서 실행...");
    }

    @Test
    @EnabledOnJre({JRE.JAVA_10, JRE.JAVA_11})
    void EnabledOnJre() {
        System.out.println("JRE 버전에 따라서 실행...");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "local", matches = "local")
    void EnabledOnIf() {
        System.out.println("Env 에따라서 실행...");
    }

}

static class StudyConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
        assertEquals(Study.class, aClass, "Can only convert to Study");
        return new Study(Integer.parseInt(o.toString()));
    }


    @DisplayName("컨버터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message = {0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest2(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }

    @Test
    void createNewStudy(@Mock MemberService memberService,
                        @Mock StudyRepository studyRepository) {

        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngsoo@naver.com");

        Optional<Member> byId = memberService.findById(1L);
        when(memberService.findById(1L))
                .thenReturn(Optional.of(member));
        Study study = new Study(10, "java");

        Optional<Member> findById = memberService.findById(1L);
        assertEquals("youngsoo@naver.com", findById.get().getEmail());

    }
}