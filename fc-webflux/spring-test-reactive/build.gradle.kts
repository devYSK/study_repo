plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // reactor
    implementation("io.projectreactor:reactor-core:3.6.6")

    // r2dbc
    implementation("io.asyncer:r2dbc-mysql:0.9.2")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // lombok
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")

    // mockito
    testImplementation("org.mockito:mockito-core")

    // reactor test
    testImplementation("io.projectreactor:reactor-test")

    // r2dbc h2
    testImplementation("io.r2dbc:r2dbc-h2")

    // h2
    testRuntimeOnly("com.h2database:h2")

    // embedded mongo
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")

    // testcontainer
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:r2dbc")

    // mysql
    testImplementation("mysql:mysql-connector-java")

    // mockWebServer
    testImplementation("com.squareup.okhttp3:mockwebserver")
}

