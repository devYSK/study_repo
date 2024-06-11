import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.serialization") version "1.8.10"
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // coroutine
    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3")

    // reactor
    implementation("io.projectreactor:reactor-core:3.6.6")

    // rxjava3
    implementation("io.reactivex.rxjava3:rxjava")

    // mutiny// https://mvnrepository.com/artifact/io.smallrye.reactive/mutiny
    implementation("io.smallrye.reactive:mutiny:2.6.0")
// https://mvnrepository.com/artifact/io.smallrye.reactive/mutiny-kotlin
    implementation("io.smallrye.reactive:mutiny-kotlin:2.6.0")


    // spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // reactor tool
    implementation("io.projectreactor:reactor-tools")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "11"
    }
}