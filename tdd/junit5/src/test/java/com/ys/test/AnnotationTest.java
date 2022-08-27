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
