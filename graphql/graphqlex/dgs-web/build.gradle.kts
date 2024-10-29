plugins {
    id("com.netflix.dgs.codegen") version "6.2.1"
    kotlin("plugin.jpa") version "1.9.25"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

extra["netflixDgsVersion"] = "9.1.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")

    implementation("com.netflix.graphql.dgs:graphql-dgs-pagination")

    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-validation")


    implementation("net.datafaker:datafaker:2.4.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")

    implementation("com.h2database:h2") // H2 데이터베이스 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-security")
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}



tasks.generateJava {
    language = "kotlin" // 'language'는 생성될 코드의 언어를 지정 GraphQL 스키마에서 Kotlin 클래스를 생성
    generateKotlinNullableClasses = true // true로 설정 시, nullable 필드는 Kotlin의 nullable 타입(?)으로 변환
    generateKotlinClosureProjections =
        true // Kotlin의 closure(람다) 프로젝션을 생성할지 여부를 설정 GraphQL의 필드를 Kotlin의 closure 형태로 생성하여, 더 유연하게 사용할 수 있음
    schemaPaths.add("${projectDir}/src/main/resources/schema")
    packageName = "com.yscorp.dgsweb.codegen"
    generateClient = true

    typeMapping = mutableMapOf(
        "CustomerConnection"    to "graphql.relay.Connection<com.yscorf.dgsweb.generated.types.Customer>",
        "Upload" to "org.springframework.web.multipart.MultipartFile",
        "Date" to "java.time.LocalDate",
        "NonNegativeInt" to "kotlin.Int",
        "Url" to "java.net.URL",
        "DateTime" to "java.time.OffsetDateTime"
    )

//	typeMapping = mutableMapOf(
//		"Date" to "java.time.LocalDate",
//		"DateTime" to "java.time.LocalDateTime",
//		"NonPositiveInt" to "java.lang.Integer",
//		"NonPositiveFloat" to "java.lang.Double",
//		"NonNegativeInt" to "java.lang.Integer",
//		"NonNegativeFloat" to "java.lang.Double",
//		"PositiveInt" to "java.lang.Integer",
//		"PositiveFloat" to "java.lang.Double",
//		"NegativeInt" to "java.lang.Integer",
//		"NegativeFloat" to "java.lang.Double",
//		"ManufacturerConnection" to "graphql.relay.SimpleListConnection<Manufacturer>",
//		"SeriesConnection" to "graphql.relay.SimpleListConnection<Series>",
//		"ModelConnection" to "graphql.relay.Connection<com.carmazing.product.datasource.entity.Model>"
//	)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
