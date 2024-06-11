plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":campusgram-service:article:core"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.cloud:" +
            "spring-cloud-starter-circuitbreaker-reactor-resilience4j")

    // spring cloud stream
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")

    // r2dbc
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // testcontainer
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")

    // reactor
    testImplementation("io.projectreactor:reactor-test")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
    }
}