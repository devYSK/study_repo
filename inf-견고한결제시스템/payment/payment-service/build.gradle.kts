//
//dependencies {
//  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
//  implementation("org.springframework.boot:spring-boot-starter-webflux")
//  implementation("org.springframework.boot:spring-boot-starter-actuator")
//  implementation("org.springframework.cloud:spring-cloud-stream")
//  implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-reactive")
//  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
//  implementation("org.jetbrains.kotlin:kotlin-reflect")
//  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//  implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
//  runtimeOnly("com.mysql:mysql-connector-j")
//  runtimeOnly("io.asyncer:r2dbc-mysql")
//  testImplementation("org.springframework.boot:spring-boot-starter-test")
//  testImplementation("io.projectreactor:reactor-test")
//  testImplementation("io.mockk:mockk:1.13.2")
//}
//
//extra["springCloudVersion"] = "2023.0.0"
//
//dependencyManagement {
//  imports {
//    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//  }
//}
//
//tasks.withType<KotlinCompile> {
//  kotlinOptions {
//    freeCompilerArgs += "-Xjsr305=strict"
//    jvmTarget = "17"
//  }
//}
//
//tasks.withType<Test> {
//  useJUnitPlatform()
////  excludeTags("TooLongTime")
////  excludeTags("ExternalIntegration")
//}


dependencies {
  implementation("org.springframework.boot:spring-boot-starter")

  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  implementation("org.jetbrains.kotlin:kotlin-reflect")

  runtimeOnly("com.mysql:mysql-connector-j")
  runtimeOnly("io.asyncer:r2dbc-mysql")

  implementation("org.springframework.cloud:spring-cloud-stream")
  implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-reactive")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

  implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("io.mockk:mockk:1.13.2")
}


extra["springCloudVersion"] = "2023.0.0"

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}
