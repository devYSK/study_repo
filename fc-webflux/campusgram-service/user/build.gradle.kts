plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // spring cloud stream
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")

    // reactor tool
    implementation("io.projectreactor:reactor-tools")

    // rxjava
    implementation("io.reactivex.rxjava3:rxjava")

    if (System.getProperty("os.arch") == "aarch64" && System.getProperty("os.name") == "Mac OS X") {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:osx-aarch_64")
    }

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

//    // lombok
//    testAnnotationProcessor("org.projectlombok:lombok")
//    testCompileOnly("org.projectlombok:lombok")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
    }
}