plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:" +
            "spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // lombok
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")

    // mockito
    testImplementation("org.mockito:mockito-core")

    // reactor test
    testImplementation("io.projectreactor:reactor-test")

    // mockWebServer
    testImplementation("com.squareup.okhttp3:mockwebserver")

    // spring-cloud-stream
//    testImplementation("org.springframework.cloud:spring-cloud-stream") {
//        artifact {
//            name = "spring-cloud-stream"
//            extension = "jar"
//            classifier = "test-binder"
//        }
//    }
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-stream-test-support
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support:4.1.1")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
    }
}