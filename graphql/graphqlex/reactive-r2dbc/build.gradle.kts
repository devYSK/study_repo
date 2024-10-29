
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    implementation("com.graphql-java:graphql-java-extended-scalars:18.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
}
