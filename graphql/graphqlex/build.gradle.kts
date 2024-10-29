plugins {
    kotlin("jvm") version "2.0.0" // 최신 버전 확인 필요
    kotlin("plugin.spring") version "2.0.0" // 최신 버전 확인 필요
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

allprojects {
    group = "com.yscorp"
    version = "1.0.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

tasks.getByName("bootJar") {
    enabled = false
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }
    val kotlinCoroutinesVersion = "1.8.1"

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinCoroutinesVersion")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
// https://mvnrepository.com/artifact/org.mockito.kotlin/mockito-kotlin
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        // testImplementation("org.mockito.kotlin:mockito-kotlin")

        implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        implementation("org.springframework.boot:spring-boot-starter-validation")
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}
//dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-graphql")
//    testImplementation("org.springframework.graphql:spring-graphql-test")
//}
