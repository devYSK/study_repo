import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3")
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-jdk8
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/lincheck-jvm
    implementation("org.jetbrains.kotlinx:lincheck-jvm:2.32")


    // reactor
    implementation("io.projectreactor:reactor-core:3.6.6")

    // rxjava3
    implementation("io.reactivex.rxjava3:rxjava")

    // mutiny// https://mvnrepository.com/artifact/io.smallrye.reactive/mutiny
    implementation("io.smallrye.reactive:mutiny:2.6.0")
// https://mvnrepository.com/artifact/io.smallrye.reactive/mutiny-kotlin
    implementation("io.smallrye.reactive:mutiny-kotlin:2.6.0")
    // SLF4J API 및 Logback 구현체 추가
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}