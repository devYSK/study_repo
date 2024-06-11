dependencies {
    // jakarta.inject
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    // nullable
    implementation("com.github.spotbugs:spotbugs-annotations:4.8.5")

    // reactor
    implementation("io.projectreactor:reactor-core:3.6.6")

    /* test */

    // mockito
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")

    // reactor
    testImplementation("io.projectreactor:reactor-test")

    // lombok
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
}