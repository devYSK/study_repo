plugins {
    kotlin("plugin.jpa") version "2.0.0" // Kotlin의 JPA 관련 플러그인
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework:spring-webflux")

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.graphql-java:graphql-java-extended-scalars:21.0")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("io.projectreactor.netty:reactor-netty-http")
}
