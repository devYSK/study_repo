plugins {
    java
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    
    // coroutine
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3")

    // reactor tool
    implementation("io.projectreactor:reactor-tools")

    // rxjava
    implementation("io.reactivex.rxjava3:rxjava")

    if (System.getProperty("os.arch") == "aarch64" && System.getProperty("os.name") == "Mac OS X") {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:osx-aarch_64")
    }

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}