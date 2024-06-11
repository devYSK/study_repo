import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.spring")
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3")

    // reactor
    implementation("io.projectreactor:reactor-core:3.6.6")

    // rxjava3
    implementation("io.reactivex.rxjava3:rxjava")

    // mutiny
    implementation("io.smallrye.reactive:mutiny")
    implementation("io.smallrye.reactive:mutiny-kotlin")

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